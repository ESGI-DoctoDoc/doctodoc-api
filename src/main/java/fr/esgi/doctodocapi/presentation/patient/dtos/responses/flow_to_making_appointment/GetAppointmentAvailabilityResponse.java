package fr.esgi.doctodocapi.presentation.patient.dtos.responses.flow_to_making_appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentAvailabilityResponse(
        UUID slotId,
        LocalDate date,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime end,
        boolean isBooked
) {
}
