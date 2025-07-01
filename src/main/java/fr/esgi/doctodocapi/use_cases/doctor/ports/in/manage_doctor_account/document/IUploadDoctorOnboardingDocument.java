package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.document.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUploadedDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUrlUploadForDoctorDocumentResponse;

import java.util.UUID;

public interface IUploadDoctorOnboardingDocument {
    GetUploadedDoctorDocumentResponse execute(SaveDocumentRequest request);
    GetUrlUploadForDoctorDocumentResponse getPresignedUrlToUpload(UUID documentId);
}
