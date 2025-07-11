package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetReportResponse(
        UUID id,
        GetReporterResponse reporter,
        String explanation,
        String status,
        LocalDateTime createdAt
) {
}
