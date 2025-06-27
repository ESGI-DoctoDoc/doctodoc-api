package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.List;
import java.util.UUID;

public record GetDoctorAppointmentDetailsResponse(
        UUID id,
        PatientInfo patient,
        MedicalConcernInfo medicalConcern,
        GetCareTrackingResponse careTracking,
        List<AnswerInfo> answers,
        String start,
        String startHour,
        String status,
        String doctorNotes,
        String createdAt
) {
}
