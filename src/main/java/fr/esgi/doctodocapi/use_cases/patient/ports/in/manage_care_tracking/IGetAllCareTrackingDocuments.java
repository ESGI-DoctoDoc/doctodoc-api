package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;

import java.util.List;
import java.util.UUID;

public interface IGetAllCareTrackingDocuments {
    List<GetDocumentResponse> process(UUID careTrackingId, String type, int page, int size);
}
