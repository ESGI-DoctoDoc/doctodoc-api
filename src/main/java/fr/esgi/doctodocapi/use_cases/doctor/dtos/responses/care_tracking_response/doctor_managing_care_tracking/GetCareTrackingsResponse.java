package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking;

import java.util.List;
import java.util.UUID;

public record GetCareTrackingsResponse(
        UUID id,
        String name,
        String createdAt,
        CareTrackingPatientInfo patient,
        List<AppointmentInfo> appointments
) {
}
