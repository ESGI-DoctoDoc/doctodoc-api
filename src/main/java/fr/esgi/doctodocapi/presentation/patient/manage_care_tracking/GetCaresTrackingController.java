package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDetailed;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetCaresTrackingController {
    private final IGetPatientCareTrackings getPatientCareTrackings;
    private final IGetPatientCareTrackingDetailed getPatientCareTrackingDetailed;

    public GetCaresTrackingController(IGetPatientCareTrackings getPatientCareTrackings, IGetPatientCareTrackingDetailed getPatientCareTrackingDetailed) {
        this.getPatientCareTrackings = getPatientCareTrackings;
        this.getPatientCareTrackingDetailed = getPatientCareTrackingDetailed;
    }

    @GetMapping("patients/care-trackings")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetPatientCareTrackingResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getPatientCareTrackings.process(page, size);
    }

    @GetMapping("patients/care-trackings/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public GetPatientCareTrackingDetailedResponse get(@PathVariable UUID id) {
        return this.getPatientCareTrackingDetailed.process(id);
    }
}
