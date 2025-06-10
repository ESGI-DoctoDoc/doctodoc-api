package fr.esgi.doctodocapi.use_cases.patient.dtos.requests.save_appointment;

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
