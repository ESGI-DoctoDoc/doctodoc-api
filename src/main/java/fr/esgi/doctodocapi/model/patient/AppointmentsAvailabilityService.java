package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentsAvailabilityService {
    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentsAvailabilityService(AppointmentRepository appointmentRepository, SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    private static List<Appointment> sortAppointmentsByStartHour(List<Appointment> appointments) {
        return appointments.stream()
                .sorted(Comparator.comparing(appointment -> appointment.getHoursRange().getStart()))
                .toList();
    }

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
                    isReserved = true;
                    break;
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

    // to optimize we can split by 5 minutes
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
