package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;

public interface IUploadDocument {
    GetUrlUploadResponse getPresignedUrlToUpload(String filename);

    void createDocument(SaveDocumentRequest saveDocumentRequest);
}
