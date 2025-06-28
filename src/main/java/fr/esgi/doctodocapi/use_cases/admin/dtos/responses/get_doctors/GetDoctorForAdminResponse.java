package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SpecialityInfo;

import java.util.UUID;

public record GetDoctorForAdminResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String birthdate,
        String rpps,
        boolean isVerified,
        String createdAt,
        SpecialityInfo speciality,
        AddressInfo address
) {
}
