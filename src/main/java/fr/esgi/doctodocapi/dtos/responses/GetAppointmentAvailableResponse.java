package fr.esgi.doctodocapi.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetAppointmentAvailableResponse(
        LocalDate date,
        LocalTime startHour,
        LocalTime endHour,
        boolean isBooked
) {
}
