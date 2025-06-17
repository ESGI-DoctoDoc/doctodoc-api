package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.UUID;

public record PatientInfo(
        UUID id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String birthdate
) {}
