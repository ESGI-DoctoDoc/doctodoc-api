package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentDetailResponse;

import java.util.UUID;

public interface IGetPatientCareTrackingDocumentDetail {
    GetDocumentDetailResponse process(UUID careTrackingId, UUID id);
}
