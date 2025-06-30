package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetUrlUploadForCareTrackingResponse;

import java.util.UUID;

public interface IUploadCareTrackingDocument {
    GetDocumentForCareTrackingResponse execute(UUID careTrackingId, SaveDocumentRequest request);
    GetUrlUploadForCareTrackingResponse getPresignedUrlToUpload(UUID id);
}
