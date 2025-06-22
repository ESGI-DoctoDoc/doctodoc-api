package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;

import java.util.UUID;

public interface IUploadMedicalRecordDocument {
    CreateMedicalRecordDocumentResponse createDocument(SaveDocumentRequest saveDocumentRequest);

    GetUrlUploadResponse getPresignedUrlToUpload(UUID id);
}
