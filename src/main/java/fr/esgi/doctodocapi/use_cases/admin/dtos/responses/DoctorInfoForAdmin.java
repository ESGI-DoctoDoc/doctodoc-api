package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import java.util.UUID;

public record DoctorInfoForAdmin(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
