package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;

import java.util.List;
import java.util.UUID;

public interface IGetAllCareTrackingDocuments {
    List<GetDocumentsOfCareTrackingResponse> process(UUID careTrackingId);
}
