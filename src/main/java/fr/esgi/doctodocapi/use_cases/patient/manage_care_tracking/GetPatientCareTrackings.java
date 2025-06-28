package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetPatientCareTrackings implements IGetPatientCareTrackings {
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;


    public GetPatientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public List<GetPatientCareTrackingResponse> process(int page, int size) {
        try {
            Patient patient = this.getPatientFromContext.get();

            List<CareTracking> caresTracking = this.careTrackingRepository.findAllByPatientId(patient.getId(), page, size);

            return caresTracking.stream().map(careTracking ->
                    new GetPatientCareTrackingResponse(
                            careTracking.getId(),
                            careTracking.getCaseName(),
                            careTracking.getDescription()
                    )).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
