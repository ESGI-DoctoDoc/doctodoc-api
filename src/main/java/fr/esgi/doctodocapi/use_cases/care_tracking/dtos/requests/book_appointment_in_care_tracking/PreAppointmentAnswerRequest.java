package fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.book_appointment_in_care_tracking;

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
