package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import java.util.List;
import java.util.UUID;

public record GetPatientCareTrackingResponse(
        UUID id,
        String name,
        String description,
        List<GetAppointmentOfCareTrackingResponse> appointments
) {
}
