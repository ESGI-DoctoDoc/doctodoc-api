package fr.esgi.doctodocapi.dtos.responses.appointment_response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentResponse(
        UUID id,
        LocalDate date,
        LocalTime hour,
        String address,
        GetAppointmentDoctorResponse doctor
) {
}