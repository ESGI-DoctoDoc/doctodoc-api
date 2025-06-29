package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetUrlUploadForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;

import java.util.UUID;

public interface IUploadCareTrackingDocument {
    GetDocumentForCareTrackingResponse execute(UUID careTrackingId, SaveDocumentRequest request);
    GetUrlUploadForCareTrackingResponse getPresignedUrlToUpload(UUID id);
}
