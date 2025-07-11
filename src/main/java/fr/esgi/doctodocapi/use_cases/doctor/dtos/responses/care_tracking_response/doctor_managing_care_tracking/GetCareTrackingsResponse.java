package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking;

import java.util.List;
import java.util.UUID;

public record GetCareTrackingsResponse(
        UUID id,
        String name,
        String description,
        String createdAt,
        String closedAt,
        List<String> files,
        CareTrackingPatientInfo patient,
        List<CareTrackingDoctorInfo> doctors,
        List<AppointmentInfo> appointments,
        CareTrackingDoctorInfo owner
) {
}
