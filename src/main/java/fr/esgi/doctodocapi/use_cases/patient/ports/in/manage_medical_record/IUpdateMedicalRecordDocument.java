package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;

import java.util.UUID;

public interface IUpdateMedicalRecordDocument {
    GetDocumentResponse process(UUID id, SaveDocumentRequest saveDocumentRequest);
}
