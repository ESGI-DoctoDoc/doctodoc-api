package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.presentation.patient.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing appointment availability.
 * This service provides functionality to find available appointment slots based on medical concerns and dates,
 * taking into account existing appointments to prevent overlaps.
 */
@Service
public class AppointmentsAvailabilityService {
    public static final int LOCKED_MAX_TIME_IN_MINUTE = 5;
    /**
     * Repository for accessing appointment data.
     */
    private final AppointmentRepository appointmentRepository;

    /**
     * Repository for accessing slot data.
     */
    private final SlotRepository slotRepository;

    /**
     * Constructs an AppointmentsAvailabilityService with the required repositories.
     *
     * @param appointmentRepository Repository for appointment data access
     * @param slotRepository        Repository for slot data access
     */
    public AppointmentsAvailabilityService(AppointmentRepository appointmentRepository, SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    /**
     * Sorts a list of appointments by their start time in ascending order.
     *
     * @param appointments The list of appointments to sort
     * @return A new sorted list of appointments
     */
    private static List<Appointment> sortAppointmentsByStartHour(List<Appointment> appointments) {
        return appointments.stream()
                .sorted(Comparator.comparing(appointment -> appointment.getHoursRange().getStart()))
                .toList();
    }

    /**
     * Retrieves available appointment slots for a specific medical concern on a given date.
     * The method finds all slots matching the medical concern and date, then determines
     * which time slots are available by checking against existing appointments.
     *
     * @param medicalConcern The medical concern for which to find available appointments
     * @param date The date for which to find available appointments
     * @return A list of available appointment slots with their reservation status
     */
    public List<GetAppointmentAvailabilityResponse> getAvailableAppointment(MedicalConcern medicalConcern, LocalDate date) {
        List<GetAppointmentAvailabilityResponse> appointmentsAvailable = new ArrayList<>();
        List<Slot> slots = this.slotRepository.getSlotsByMedicalConcernAndDate(medicalConcern.getId(), date);

        slots.forEach(slot -> {
            List<GetAppointmentAvailabilityResponse> defaultAppointmentsAvailable = getSlotDivision(medicalConcern, slot);
            List<Appointment> appointments = this.appointmentRepository.getAppointmentsBySlot(slot.getId());

            if (appointments.isEmpty()) {
                appointmentsAvailable.addAll(defaultAppointmentsAvailable);
            } else {

                List<GetAppointmentAvailabilityResponse> result = calculateAvailableAppointments(slot.getId(), defaultAppointmentsAvailable, appointments);
                appointmentsAvailable.addAll(result);
            }

        });

        return appointmentsAvailable;

    }

    /**
     * Calculates which appointment slots are available by checking for overlaps with existing appointments.
     * For each potential appointment slot, this method checks if it overlaps with any existing appointment
     * and marks it as reserved if an overlap is found.
     *
     * @param slotId The ID of the slot being processed
     * @param defaultAppointmentsAvailable The list of potential appointment slots
     * @param appointments The list of existing appointments to check against
     * @return A list of appointment slots with their reservation status updated
     */
    private List<GetAppointmentAvailabilityResponse> calculateAvailableAppointments(UUID slotId, List<GetAppointmentAvailabilityResponse> defaultAppointmentsAvailable, List<Appointment> appointments) {
        List<GetAppointmentAvailabilityResponse> appointmentsAvailable = new ArrayList<>();
        List<Appointment> sortedAppointments = sortAppointmentsByStartHour(appointments);

        for (GetAppointmentAvailabilityResponse slotFragment : defaultAppointmentsAvailable) {
            HoursRange slotFragmentHoursRange = HoursRange.of(slotFragment.start(), slotFragment.end());
            boolean isReserved = false;

            for (Appointment appointment : sortedAppointments) {
                if (appointment.getHoursRange().getStart().isAfter(slotFragment.end())) {
                    break;
                }

                if (HoursRange.isTimesOverlap(slotFragmentHoursRange, appointment.getHoursRange())) {
                    if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
                        isReserved = true;
                        break;
                    }

                    if (appointment.getStatus() == AppointmentStatus.LOCKED) {
                        LocalDateTime unlockedTime = appointment.getLockedAt().plusMinutes(LOCKED_MAX_TIME_IN_MINUTE);
                        LocalDateTime now = LocalDateTime.now();
                        if (unlockedTime.isAfter(now)) {
                            isReserved = true;
                            break;
                        }
                    }

                }


            }

            appointmentsAvailable.add(
                    new GetAppointmentAvailabilityResponse(
                            slotId,
                            slotFragment.date(),
                            slotFragment.start(),
                            slotFragment.end(),
                            isReserved
                    ));
        }

        return appointmentsAvailable;
    }

    /**
     * Divides a slot into smaller appointment time slots based on the duration required for a medical concern.
     * This method creates potential appointment slots by dividing the slot's time range into segments
     * of the duration specified by the medical concern.
     *
     * @param medicalConcern The medical concern that determines the duration of each appointment
     * @param slot The slot to divide into smaller appointment time slots
     * @return A list of potential appointment slots
     */
    private List<GetAppointmentAvailabilityResponse> getSlotDivision(MedicalConcern medicalConcern, Slot slot) {
        List<GetAppointmentAvailabilityResponse> appointmentsAvailable = new ArrayList<>();

        Integer duration = medicalConcern.getDurationInMinutes().getValue();
        LocalTime startHour = slot.getHoursRange().getStart();
        LocalTime endHour = slot.getHoursRange().getEnd();

        LocalTime currentStart = startHour;
        while (!currentStart.plusMinutes(duration).isAfter(endHour)) {
            LocalTime availableAppointmentEnd = currentStart.plusMinutes(duration);
            appointmentsAvailable.add(new GetAppointmentAvailabilityResponse(slot.getId(), slot.getDate(), currentStart, availableAppointmentEnd, false));
            currentStart = availableAppointmentEnd;
        }

        return appointmentsAvailable;
    }
}
