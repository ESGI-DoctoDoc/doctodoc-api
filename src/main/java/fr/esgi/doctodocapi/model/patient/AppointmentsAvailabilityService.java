package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.dtos.responses.GetAppointmentAvailableResponse;
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

    public List<GetAppointmentAvailableResponse> getAvailableAppointment(MedicalConcern medicalConcern, LocalDate date) {
        List<GetAppointmentAvailableResponse> appointmentsAvailable = new ArrayList<>();
        List<Slot> slots = this.slotRepository.getSlotsByMedicalConcernAndDate(medicalConcern.getId(), date);

        slots.forEach(slot -> {
            List<GetAppointmentAvailableResponse> defaultAppointmentsAvailable = getSlotDivision(medicalConcern, slot);
            List<Appointment> appointments = this.appointmentRepository.getAppointmentsBySlot(slot.getId());

            if (appointments.isEmpty()) {
                appointmentsAvailable.addAll(defaultAppointmentsAvailable);
            } else {

                List<GetAppointmentAvailableResponse> result = calculateAvailableAppointments(defaultAppointmentsAvailable, appointments);
                appointmentsAvailable.addAll(result);
            }

        });

        return appointmentsAvailable;

    }

    private List<GetAppointmentAvailableResponse> calculateAvailableAppointments(List<GetAppointmentAvailableResponse> defaultAppointmentsAvailable, List<Appointment> appointments) {
        List<GetAppointmentAvailableResponse> appointmentsAvailable = new ArrayList<>();
        List<Appointment> sortedAppointments = sortAppointmentsByStartHour(appointments);

        for (GetAppointmentAvailableResponse slotFragment : defaultAppointmentsAvailable) {
            HoursRange slotFragmentHoursRange = HoursRange.of(slotFragment.startHour(), slotFragment.endHour());
            boolean isReserved = false;

            for (Appointment appointment : sortedAppointments) {
                if (appointment.getHoursRange().getStart().isAfter(slotFragment.endHour())) {
                    break;
                }
                if (HoursRange.isTimesOverlap(slotFragmentHoursRange, appointment.getHoursRange())) {
                    isReserved = true;
                    break;
                }
            }

            appointmentsAvailable.add(
                    new GetAppointmentAvailableResponse(
                            slotFragment.date(),
                            slotFragment.startHour(),
                            slotFragment.endHour(),
                            isReserved
                    ));
        }

        return appointmentsAvailable;
    }

    private List<GetAppointmentAvailableResponse> getSlotDivision(MedicalConcern medicalConcern, Slot slot) {
        List<GetAppointmentAvailableResponse> appointmentsAvailable = new ArrayList<>();

        Integer duration = medicalConcern.getDurationInMinutes().getValue();
        LocalTime startHour = slot.getHoursRange().getStart();
        LocalTime endHour = slot.getHoursRange().getEnd();

        LocalTime currentStart = startHour;
        while (!currentStart.plusMinutes(duration).isAfter(endHour)) {
            LocalTime availableAppointmentEnd = currentStart.plusMinutes(duration);
            appointmentsAvailable.add(new GetAppointmentAvailableResponse(slot.getDate(), currentStart, availableAppointmentEnd, false));
            currentStart = availableAppointmentEnd;
        }

        return appointmentsAvailable;
    }
}
