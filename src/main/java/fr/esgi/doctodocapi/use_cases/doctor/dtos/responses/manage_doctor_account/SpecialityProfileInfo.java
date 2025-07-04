package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account;

import java.util.UUID;

public record SpecialityProfileInfo(
        UUID id,
        String name
) {
}
