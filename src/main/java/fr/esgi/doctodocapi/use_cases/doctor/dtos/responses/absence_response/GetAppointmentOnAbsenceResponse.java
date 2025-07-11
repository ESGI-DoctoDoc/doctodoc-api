package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response;

import java.util.UUID;

public record GetAppointmentOnAbsenceResponse(
        UUID id,
        String start,
        String startHour,
        String endHour,
        AppointmentPatient patient
) {
    public record AppointmentPatient(
            UUID id,
            String name
    ) {
    }
}
