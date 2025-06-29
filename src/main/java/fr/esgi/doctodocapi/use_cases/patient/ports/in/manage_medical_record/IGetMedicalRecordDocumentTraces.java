package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetMedicalRecordDocumentTracesResponse;

import java.util.List;
import java.util.UUID;

public interface IGetMedicalRecordDocumentTraces {
    List<GetMedicalRecordDocumentTracesResponse> getAll(UUID documentId);
}
