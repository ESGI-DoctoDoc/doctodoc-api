package fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses;

import java.util.UUID;

public record CareTrackingPatientInfo(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
