package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking;

import java.util.UUID;

public record AppointmentInfo(
        UUID id,
        MedicalConcernInfoForCareTracking medicalConcern,
        String start,
        String startHour,
        String status,
        String doctorNotes
) {
}
