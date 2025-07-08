package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search;

import java.util.UUID;

public record GetSearchAppointmentResponse(
        UUID id,
        AppointmentDoctor doctor,
        AppointmentPatient patient,
        AppointmentMedicalConcern medicalConcern,
        String start,
        String startHour,
        String endHour,
        String status,
        String createdAt
) {
    public record AppointmentDoctor(
            UUID id,
            String firstName,
            String lastName,
            String email
    ) {}

    public record AppointmentPatient(
            UUID id,
            String name,
            String email,
            String phone,
            String birthdate
    ) {}

    public record AppointmentMedicalConcern(
            UUID id,
            String name
    ) {}
}