package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.DocumentResponseMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetAllCareTrackingDocuments;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetAllCareTrackingDocuments implements IGetAllCareTrackingDocuments {
    private final CareTrackingRepository careTrackingRepository;
    private final DocumentResponseMapper documentResponseMapper;
    private final GetPatientFromContext getPatientFromContext;

    public GetAllCareTrackingDocuments(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, DocumentResponseMapper documentResponseMapper) {
        this.careTrackingRepository = careTrackingRepository;
        this.documentResponseMapper = documentResponseMapper;
        this.getPatientFromContext = getPatientFromContext;
    }

    public List<GetDocumentResponse> process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(id, patient);
            return careTracking.getDocuments().stream().map(this.documentResponseMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
