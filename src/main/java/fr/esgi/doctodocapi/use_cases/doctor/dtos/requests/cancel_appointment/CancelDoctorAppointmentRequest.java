package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.cancel_appointment;

import jakarta.validation.constraints.NotBlank;

public record CancelDoctorAppointmentRequest(
        @NotBlank
        String reason
) {
}
