package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.UUID;

public record GetDoctorAppointmentResponse(
        UUID id,
        PatientInfo patient,
        MedicalConcernInfo medicalConcern,
        String start,
        String startHour,
        String endHour,
        String status,
        String doctorNotes,
        String createdAt
) {
}