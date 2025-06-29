package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_managing_care_tracking;

import java.util.UUID;

public record CareTrackingPatientInfo(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
