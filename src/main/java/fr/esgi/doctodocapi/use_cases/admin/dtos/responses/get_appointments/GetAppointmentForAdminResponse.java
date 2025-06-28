package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.DoctorInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetCareTrackingForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_medical_concerns.MedicalConcernInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.PatientInfoForAdmin;

import java.util.List;
import java.util.UUID;

public record GetAppointmentForAdminResponse(
        UUID id,
        DoctorInfoForAdmin doctor,
        PatientInfoForAdmin patient,
        MedicalConcernInfoForAdmin medicalConcern,
        GetCareTrackingForAdminResponse careTracking,
        List<AnswerInfoForAdmin> answers,
        String start,
        String startHour,
        String endHour,
        String status,
        String doctorNotes,
        String createdAt
) {
}
