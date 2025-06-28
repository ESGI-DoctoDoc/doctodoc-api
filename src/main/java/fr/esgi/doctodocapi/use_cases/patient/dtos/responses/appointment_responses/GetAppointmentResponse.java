package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentResponse(
        UUID id,
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime end,
        String address,
        GetAppointmentDoctorResponse doctor
) {
}