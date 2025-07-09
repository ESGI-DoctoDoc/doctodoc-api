package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.get_appointment_availability_response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetTodayAppointmentAvailabilityResponse(
        UUID slotId,
        LocalDate date,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime end,
        boolean isBooked
) {
}
