package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record UpdateDoctorAppointmentRequest(
        @NotNull UUID medicalConcernId,
        @NotNull LocalDate start,
        @NotNull LocalTime startHour,
        String notes,
        UUID careTrackingId,
        List<UpdateDoctorAnswersForAnAppointmentRequest> answers
) {
    public UpdateDoctorAppointmentRequest {
        if (answers == null) {
            answers = List.of();
        }
    }

    public record UpdateDoctorAnswersForAnAppointmentRequest(
            @NotBlank UUID questionId,
            @NotBlank String answer
    ) {
        public UpdateDoctorAnswersForAnAppointmentRequest {
            answer = answer.trim();
        }
    }
}
