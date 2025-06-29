package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetDocumentForCareTrackingResponse;

import java.util.UUID;

public interface IGetCareTrackingDocumentContent {
    GetDocumentForCareTrackingResponse execute(UUID careTrackingId, UUID documentId);
}
