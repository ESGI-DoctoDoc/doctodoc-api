package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search;

import java.util.UUID;

public record GetDoctorPatientResponse(
        UUID id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String gender
) {
}