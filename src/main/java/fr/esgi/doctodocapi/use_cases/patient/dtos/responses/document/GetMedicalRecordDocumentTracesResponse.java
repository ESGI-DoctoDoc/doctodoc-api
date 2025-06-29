package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GetMedicalRecordDocumentTracesResponse(
        String type,
        String description,
        GetMedicalRecordDocumentUser user,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime date
) {
}
