package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SaveDoctorAnswersForAnAppointmentRequest(
        @NotBlank UUID questionId,
        @NotBlank String answer
) {
    public SaveDoctorAnswersForAnAppointmentRequest {
        answer = answer.trim();
    }
}

