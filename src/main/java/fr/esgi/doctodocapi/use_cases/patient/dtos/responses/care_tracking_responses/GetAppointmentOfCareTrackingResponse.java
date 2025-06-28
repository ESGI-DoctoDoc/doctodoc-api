package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import java.time.LocalDate;
import java.util.UUID;

public record GetAppointmentOfCareTrackingResponse(
        UUID id,
        GetDoctorOfCareTrackingResponse doctor,
        LocalDate date
) {
}
