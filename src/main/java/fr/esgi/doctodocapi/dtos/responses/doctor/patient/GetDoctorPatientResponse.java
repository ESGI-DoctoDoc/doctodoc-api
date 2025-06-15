package fr.esgi.doctodocapi.dtos.responses.doctor.patient;

import java.time.LocalDate;
import java.util.UUID;

public record GetDoctorPatientResponse(
        UUID id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String birthDate,
        String gender,
        LocalDate createdAt
) {
}
