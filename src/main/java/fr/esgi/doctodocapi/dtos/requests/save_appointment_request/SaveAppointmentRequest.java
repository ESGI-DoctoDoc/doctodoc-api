package fr.esgi.doctodocapi.dtos.requests.save_appointment_request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SaveAppointmentRequest(
        @NotNull UUID doctorId,
        @NotNull UUID patientId,
        @NotNull UUID medicalConcernId,
        @NotNull UUID slotId,
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        List<SaveAnswersForAnAppointmentRequest> responses
) {
}
