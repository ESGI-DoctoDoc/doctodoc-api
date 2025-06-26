package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import fr.esgi.doctodocapi.model.care_tracking.CareTracking;

import java.util.UUID;

public record GetCareTrackingResponse(
        UUID id,
        String name
) {
    public static GetCareTrackingResponse fromDomain(CareTracking careTracking) {
        return new GetCareTrackingResponse(
                careTracking.getId(),
                careTracking.getCaseName()
        );
    }
}