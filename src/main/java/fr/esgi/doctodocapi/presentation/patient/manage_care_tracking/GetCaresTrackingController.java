package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetCaresTrackingController {
    private final IGetPatientCareTrackings getPatientCareTrackings;

    public GetCaresTrackingController(IGetPatientCareTrackings getPatientCareTrackings) {
        this.getPatientCareTrackings = getPatientCareTrackings;
    }

    @GetMapping("patients/care-trackings")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetPatientCareTrackingResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getPatientCareTrackings.process(page, size);
    }
}
