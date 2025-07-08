package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SpecialityInfo;

import java.util.UUID;

public record GetSearchDoctorForAdminResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        SpecialityInfo speciality,
        String createdAt
) {}
