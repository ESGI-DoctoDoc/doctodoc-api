package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.DeleteDoctorDocumentResponse;

import java.util.UUID;

public interface IDeleteDoctorDocument {
    DeleteDoctorDocumentResponse execute(UUID documentId);
}
