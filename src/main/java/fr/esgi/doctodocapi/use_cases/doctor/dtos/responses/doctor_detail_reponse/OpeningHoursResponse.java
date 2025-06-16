package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record OpeningHoursResponse(
        String day,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime start,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime end
) {
}
