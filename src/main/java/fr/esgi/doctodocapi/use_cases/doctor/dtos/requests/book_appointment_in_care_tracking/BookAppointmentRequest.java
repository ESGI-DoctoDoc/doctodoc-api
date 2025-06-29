package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.book_appointment_in_care_tracking;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record BookAppointmentRequest(
        @NotNull UUID careTrackingId,
        @NotNull UUID medicalConcernId,
        @NotNull UUID patientId,
        @NotNull LocalDate start,
        @NotNull LocalTime startHour,
        List<PreAppointmentAnswerRequest> answers,
        String notes
) {
}
