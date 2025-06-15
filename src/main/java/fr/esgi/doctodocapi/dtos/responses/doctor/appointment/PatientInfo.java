package fr.esgi.doctodocapi.dtos.responses.doctor.appointment;

import java.util.UUID;

public record PatientInfo(
        UUID id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String birthdate
) {}
