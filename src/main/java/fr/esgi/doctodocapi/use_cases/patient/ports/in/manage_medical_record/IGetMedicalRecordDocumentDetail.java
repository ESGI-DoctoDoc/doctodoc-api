package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentDetailResponse;

import java.util.UUID;

public interface IGetMedicalRecordDocumentDetail {
    GetDocumentDetailResponse process(UUID id);
}
