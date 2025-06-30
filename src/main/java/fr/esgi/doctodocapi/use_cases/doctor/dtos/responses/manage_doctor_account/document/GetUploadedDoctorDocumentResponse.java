package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document;

import java.util.UUID;

public record GetUploadedDoctorDocumentResponse(
        UUID id,
        String name,
        String type,
        String url
) {
}
