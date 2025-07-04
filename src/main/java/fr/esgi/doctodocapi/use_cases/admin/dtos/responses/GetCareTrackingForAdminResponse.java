package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import fr.esgi.doctodocapi.model.care_tracking.CareTracking;

import java.util.UUID;

public record GetCareTrackingForAdminResponse(
        UUID id,
        String name
) {
    public static GetCareTrackingForAdminResponse fromDomain(CareTracking careTracking) {
        return new GetCareTrackingForAdminResponse(
                careTracking.getId(),
                careTracking.getCaseName()
        );
    }
}
