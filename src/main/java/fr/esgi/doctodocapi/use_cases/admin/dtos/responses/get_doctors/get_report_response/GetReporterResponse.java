package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response;

import java.util.UUID;

public record GetReporterResponse(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
