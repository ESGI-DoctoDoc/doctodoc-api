package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetDoctorAppointmentAvailabilityResponse(
        UUID id,
        LocalDate date,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime end,
        boolean isBooked
) { }
