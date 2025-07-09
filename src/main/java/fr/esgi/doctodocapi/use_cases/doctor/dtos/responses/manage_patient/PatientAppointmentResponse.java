package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient;

import java.util.UUID;

public record PatientAppointmentResponse(
        UUID id,
        String date,
        String startHour,
        String endHour,
        String cancelledReason,
        String comment,
        String status
) {
}