package fr.esgi.doctodocapi.dtos.requests.save_appointment_request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SaveAnswersForAnAppointmentRequest(
        @NotBlank UUID questionId,
        @NotBlank String answer
) {
    public SaveAnswersForAnAppointmentRequest {
        answer = answer.trim();
    }
}
