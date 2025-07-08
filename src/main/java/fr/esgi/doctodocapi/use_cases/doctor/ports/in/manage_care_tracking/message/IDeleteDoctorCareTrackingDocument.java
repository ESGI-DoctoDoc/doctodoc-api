package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.DeleteCareTrackingDocumentResponse;

import java.util.UUID;

public interface IDeleteDoctorCareTrackingDocument {
    DeleteCareTrackingDocumentResponse execute(UUID carTrackingId, UUID documentId);
}
