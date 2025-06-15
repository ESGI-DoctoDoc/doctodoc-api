package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
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
import static org.mockito.Mockito.*;

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
        UUID doctorId = UUID.randomUUID();
        LocalDate createdAt = LocalDate.now();

        // generate medical concerns
        MedicalConcern medicalConcern1 = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0, doctorId, createdAt);
        MedicalConcern medicalConcern2 = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0, doctorId, createdAt);

        // generate slot
        LocalDate defaultDate = LocalDate.of(2025, 3, 10);
        LocalTime defaultHour = LocalTime.of(9, 0, 0);
        Slot slot1 = Slot.create(defaultDate, defaultHour, defaultHour.plusHours(3), List.of(medicalConcern1, medicalConcern2));

        LocalDate defaultDate2 = LocalDate.of(2025, 3, 12);
        LocalTime defaultHour2 = LocalTime.of(11, 0, 0);
        Slot slot2 = Slot.create(defaultDate2, defaultHour2, defaultHour2.plusHours(1), List.of(medicalConcern1));


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
    void should_divide_slot_and_filter_with_all_statuses() {
        UUID doctorId = UUID.randomUUID();
        LocalDate createdAt = LocalDate.now();

        // Given
        MedicalConcern medicalConcern = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0, doctorId, createdAt);
        MedicalConcern medicalConcern2 = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0, doctorId, createdAt);

        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        Slot slot = Slot.create(date, startTime, endTime, List.of(medicalConcern, medicalConcern2));

        // Appointments
        Appointment confirmed = new Appointment(
                UUID.randomUUID(), slot, null, null, medicalConcern,
                LocalTime.of(9, 0), LocalTime.of(9, 15),
                LocalDateTime.now(), AppointmentStatus.CONFIRMED, List.of(), LocalDateTime.now(), createdAt
        );

        Appointment confirmed2 = new Appointment(
                UUID.randomUUID(), slot, null, null, medicalConcern2,
                LocalTime.of(10, 0), LocalTime.of(10, 30),
                LocalDateTime.now(), AppointmentStatus.CONFIRMED, List.of(), LocalDateTime.now(), createdAt
        );

        Appointment lockedNotExpired = new Appointment(
                UUID.randomUUID(), slot, null, null, medicalConcern,
                LocalTime.of(9, 15), LocalTime.of(9, 30),
                LocalDateTime.now().minusMinutes(2),
                AppointmentStatus.LOCKED, List.of(), LocalDateTime.now().minusMinutes(2), createdAt
        );

        Appointment lockedExpired = new Appointment(
                UUID.randomUUID(), slot, null, null, medicalConcern,
                LocalTime.of(9, 30), LocalTime.of(9, 45),
                LocalDateTime.now().minusMinutes(10),
                AppointmentStatus.LOCKED, List.of(), LocalDateTime.now().minusMinutes(10), createdAt
        );

        Appointment cancelled = new Appointment(
                UUID.randomUUID(), slot, null, null, medicalConcern,
                LocalTime.of(9, 45), LocalTime.of(10, 0),
                LocalDateTime.now(), AppointmentStatus.CANCELLED, List.of(), LocalDateTime.now(), createdAt
        );

        // Expected results
        List<GetAppointmentAvailabilityResponse> expected = List.of(
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(9, 0), LocalTime.of(9, 15), true),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(9, 15), LocalTime.of(9, 30), true),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(9, 30), LocalTime.of(9, 45), false),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(9, 45), LocalTime.of(10, 0), false),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(10, 0), LocalTime.of(10, 15), true),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(10, 15), LocalTime.of(10, 30), true),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(10, 30), LocalTime.of(10, 45), false),
                new GetAppointmentAvailabilityResponse(slot.getId(), date, LocalTime.of(10, 45), LocalTime.of(11, 0), false)
        );

        // Mocks
        when(this.slotRepository.getSlotsByMedicalConcernAndDate(medicalConcern.getId(), date)).thenReturn(List.of(slot));
        when(this.appointmentRepository.getAppointmentsBySlot(slot.getId())).thenReturn(
                List.of(confirmed, confirmed2, lockedNotExpired, lockedExpired, cancelled)
        );

        // When
        List<GetAppointmentAvailabilityResponse> result = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern, date);

        // Then
        assertEquals(expected, result);
        verify(this.slotRepository).getSlotsByMedicalConcernAndDate(medicalConcern.getId(), date);
        verify(this.appointmentRepository).getAppointmentsBySlot(slot.getId());
        verifyNoMoreInteractions(this.slotRepository, this.appointmentRepository);
    }
}