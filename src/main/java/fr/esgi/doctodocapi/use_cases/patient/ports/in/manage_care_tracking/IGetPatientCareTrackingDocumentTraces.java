package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetMedicalRecordDocumentTracesResponse;

import java.util.List;
import java.util.UUID;

public interface IGetPatientCareTrackingDocumentTraces {
    List<GetMedicalRecordDocumentTracesResponse> getAll(UUID careTrackingId, UUID documentId);

}
