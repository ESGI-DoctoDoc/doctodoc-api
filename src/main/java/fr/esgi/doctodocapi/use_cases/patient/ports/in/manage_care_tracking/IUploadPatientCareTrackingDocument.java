package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;

import java.util.UUID;

public interface IUploadPatientCareTrackingDocument {
    CreateMedicalRecordDocumentResponse createDocument(UUID id, SaveDocumentRequest saveDocumentRequest);

    GetUrlUploadResponse getPresignedUrlToUpload(UUID id);
}
