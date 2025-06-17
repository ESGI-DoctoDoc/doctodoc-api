package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient;

import java.time.LocalDate;
import java.util.UUID;

public record GetDoctorPatientResponse(
        UUID id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String birthdate,
        String gender,
        LocalDate createdAt
) {
}
