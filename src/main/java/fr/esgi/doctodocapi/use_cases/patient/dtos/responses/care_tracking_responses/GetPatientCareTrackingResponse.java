package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetPatientCareTrackingResponse(
        UUID id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime closedAt,
        List<GetAppointmentOfCareTrackingResponse> appointments
) {
}
