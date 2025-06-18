package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SaveDoctorAppointmentRequest(
        @NotNull UUID patientId,
        @NotNull UUID medicalConcernId,
        @NotNull LocalDate start,
        @NotNull LocalTime startHour,
        String notes,
        List<SaveDoctorAnswersForAnAppointmentRequest> answers
) {
    public SaveDoctorAppointmentRequest {
        if (answers == null) {
            answers = List.of();
        }
    }
}
