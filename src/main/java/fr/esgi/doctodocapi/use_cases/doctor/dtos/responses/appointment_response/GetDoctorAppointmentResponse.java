package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetDoctorAppointmentResponse(
        UUID id,
        PatientInfo patient,
        String start,
        String startHour,
        String status,
        String doctorNotes,
        String createdAt
) {
}