package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentsAvailabilityServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private AppointmentsAvailabilityService appointmentsAvailabilityService;

    @Test
    void should_return_slot_divisions_if_no_appointments() {
        // generate medical concerns
        MedicalConcern medicalConcern1 = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0);
        MedicalConcern medicalConcern2 = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0);

        // generate slot
        LocalDate defaultDate = LocalDate.of(2025, 3, 10);
        LocalTime defaultHour = LocalTime.of(9, 0, 0);
        Slot slot1 = new Slot(UUID.randomUUID(), defaultDate, defaultHour, defaultHour.plusHours(3), List.of(), List.of(medicalConcern1, medicalConcern2));

        LocalDate defaultDate2 = LocalDate.of(2025, 3, 12);
        LocalTime defaultHour2 = LocalTime.of(11, 0, 0);
        Slot slot2 = new Slot(UUID.randomUUID(), defaultDate2, defaultHour2, defaultHour2.plusHours(1), List.of(), List.of(medicalConcern1));


        // response expected
        List<GetAppointmentAvailabilityResponse> expected = List.of(
                // slot 1
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 0), LocalTime.of(9, 15), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 15), LocalTime.of(9, 30), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 30), LocalTime.of(9, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 45), LocalTime.of(10, 0), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 0), LocalTime.of(10, 15), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 15), LocalTime.of(10, 30), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 30), LocalTime.of(10, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 45), LocalTime.of(11, 0), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 0), LocalTime.of(11, 15), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 15), LocalTime.of(11, 30), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 30), LocalTime.of(11, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 45), LocalTime.of(12, 0), false),

                // slot 2
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 0), LocalTime.of(11, 15), false),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 15), LocalTime.of(11, 30), false),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 30), LocalTime.of(11, 45), false),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 45), LocalTime.of(12, 0), false)
        );


        when(this.slotRepository.getSlotsByMedicalConcernAndDate(medicalConcern1.getId(), slot1.getDate())).thenReturn(List.of(slot1, slot2));

        // no appointments
        when(this.appointmentRepository.getAppointmentsBySlot(slot1.getId())).thenReturn(List.of());
        when(this.appointmentRepository.getAppointmentsBySlot(slot2.getId())).thenReturn(List.of());

        List<GetAppointmentAvailabilityResponse> results = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern1, slot1.getDate());

        assertEquals(expected, results);
        verifyNoMoreInteractions(this.slotRepository, this.appointmentRepository);
    }

    @Test
    void should_divide_slot_without_including_overlapping_appointments() {
        // generate medical concerns
        MedicalConcern medicalConcern1 = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0);
        MedicalConcern medicalConcern2 = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0);

        // generate slot
        LocalDate defaultDate = LocalDate.of(2025, 3, 10);
        LocalTime defaultHour = LocalTime.of(9, 0, 0);
        Slot slot1 = new Slot(UUID.randomUUID(), defaultDate, defaultHour, defaultHour.plusHours(3), List.of(), List.of(medicalConcern1, medicalConcern2));

        LocalDate defaultDate2 = LocalDate.of(2025, 3, 12);
        LocalTime defaultHour2 = LocalTime.of(11, 0, 0);
        Slot slot2 = new Slot(UUID.randomUUID(), defaultDate2, defaultHour2, defaultHour2.plusHours(1), List.of(), List.of(medicalConcern1));

        // generate appointment
        LocalTime startHourAppointment1 = LocalTime.of(9, 0);
        LocalTime endHourAppointment1 = LocalTime.of(9, 15);
        Appointment appointment1 = new Appointment(
                UUID.randomUUID(),
                slot1,
                null,
                null,
                medicalConcern1,
                startHourAppointment1,
                endHourAppointment1,
                LocalDateTime.of(2025, 3, 1, 10, 0),
                AppointmentStatus.CONFIRMED
        );

        LocalTime startHourAppointment2 = LocalTime.of(10, 0);
        LocalTime endHourAppointment2 = LocalTime.of(10, 30);
        Appointment appointment2 = new Appointment(
                UUID.randomUUID(),
                slot1,
                null,
                null,
                medicalConcern2,
                startHourAppointment2,
                endHourAppointment2,
                LocalDateTime.of(2025, 3, 1, 10, 0),
                AppointmentStatus.CONFIRMED
        );

        LocalTime startHourAppointment3 = LocalTime.of(11, 30);
        LocalTime endHourAppointment3 = LocalTime.of(11, 45);
        Appointment appointment3 = new Appointment(
                UUID.randomUUID(),
                slot2,
                null,
                null,
                medicalConcern1,
                startHourAppointment3,
                endHourAppointment3,
                LocalDateTime.of(2025, 3, 1, 10, 0),
                AppointmentStatus.CANCELLED
        );


        // response expected
        List<GetAppointmentAvailabilityResponse> expected = List.of(
                // slot 1
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 0), LocalTime.of(9, 15), true),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 15), LocalTime.of(9, 30), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 30), LocalTime.of(9, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(9, 45), LocalTime.of(10, 0), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 0), LocalTime.of(10, 15), true),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 15), LocalTime.of(10, 30), true),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 30), LocalTime.of(10, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(10, 45), LocalTime.of(11, 0), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 0), LocalTime.of(11, 15), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 15), LocalTime.of(11, 30), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 30), LocalTime.of(11, 45), false),
                new GetAppointmentAvailabilityResponse(slot1.getId(), defaultDate, LocalTime.of(11, 45), LocalTime.of(12, 0), false),

                // slot 2
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 0), LocalTime.of(11, 15), false),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 15), LocalTime.of(11, 30), false),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 30), LocalTime.of(11, 45), true),
                new GetAppointmentAvailabilityResponse(slot2.getId(), defaultDate2, LocalTime.of(11, 45), LocalTime.of(12, 0), false)
        );


        when(this.slotRepository.getSlotsByMedicalConcernAndDate(medicalConcern1.getId(), slot1.getDate())).thenReturn(List.of(slot1, slot2));
        when(this.appointmentRepository.getAppointmentsBySlot(slot1.getId())).thenReturn(List.of(appointment1, appointment2));
        when(this.appointmentRepository.getAppointmentsBySlot(slot2.getId())).thenReturn(List.of(appointment3));

        List<GetAppointmentAvailabilityResponse> results = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern1, slot1.getDate());

        assertEquals(expected, results);
        verifyNoMoreInteractions(this.slotRepository, this.appointmentRepository);
    }
}