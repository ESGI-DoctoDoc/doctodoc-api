package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;

import java.util.UUID;

public interface IGetPatientDocumentCareTrackingContent {
    GetDocumentsOfCareTrackingResponse process(UUID careTrackingId, UUID id);
}
