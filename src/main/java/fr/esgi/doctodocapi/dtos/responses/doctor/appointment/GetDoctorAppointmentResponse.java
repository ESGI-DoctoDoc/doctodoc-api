package fr.esgi.doctodocapi.dtos.responses.doctor.appointment;

import java.time.LocalDate;
import java.util.UUID;

public record GetDoctorAppointmentResponse(
        UUID id,
        PatientInfo patient,
        String start,
        String startHour,
        String status,
        String doctorNotes,
        LocalDate createdAt
) {
}