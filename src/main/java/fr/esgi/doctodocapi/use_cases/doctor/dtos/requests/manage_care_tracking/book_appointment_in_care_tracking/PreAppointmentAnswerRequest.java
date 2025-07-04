package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.book_appointment_in_care_tracking;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PreAppointmentAnswerRequest(
        @NotBlank UUID questionId,
        @NotBlank String answer
) {
    public PreAppointmentAnswerRequest {
        answer = answer.trim();
    }
}
