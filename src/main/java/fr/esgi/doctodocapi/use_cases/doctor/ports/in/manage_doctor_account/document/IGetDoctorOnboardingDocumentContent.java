package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetDocumentForDoctorOnboardingResponse;

import java.util.UUID;

public interface IGetDoctorOnboardingDocumentContent {
    GetDocumentForDoctorOnboardingResponse execute(UUID documentId);
}