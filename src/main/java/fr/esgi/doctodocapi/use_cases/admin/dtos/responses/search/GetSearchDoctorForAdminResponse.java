package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search;

import java.util.UUID;

public record GetSearchDoctorForAdminResponse(
        UUID id,
        String name
) {
}
