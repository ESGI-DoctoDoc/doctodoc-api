package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.UUID;

public record PatientInfo(
        UUID id,
        String name,
        String email,
        String phone,
        String birthdate
) {
    public PatientInfo {
        name = name().trim();
        email = email().trim();
        phone = phone().trim();
        birthdate = birthdate().trim();
    }
}
