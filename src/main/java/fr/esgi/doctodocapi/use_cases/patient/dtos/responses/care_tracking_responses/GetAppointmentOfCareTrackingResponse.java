package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentOfCareTrackingResponse(
        UUID id,
        GetDoctorOfCareTrackingResponse doctor,
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start
) {
}
