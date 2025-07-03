package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;

import java.util.UUID;

public interface IUpdatePatientCareTrackingDocument {
    GetDocumentResponse process(UUID careTrackingId, UUID id, SaveDocumentRequest saveDocumentRequest);
}
