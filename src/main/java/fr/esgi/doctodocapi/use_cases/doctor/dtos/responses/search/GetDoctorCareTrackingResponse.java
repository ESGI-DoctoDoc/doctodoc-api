package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search;

import java.util.UUID;

public record GetDoctorCareTrackingResponse(
        UUID id,
        String name,
        CareTrackingPatient patient,
        String endedAt
) {
    public record CareTrackingPatient(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String phone
    ) {}
}
