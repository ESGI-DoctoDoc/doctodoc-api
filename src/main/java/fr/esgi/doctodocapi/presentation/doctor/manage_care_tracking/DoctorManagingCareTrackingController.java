package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.InitializeCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.IGetCareTrackings;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.IInitializeCareTracking;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class DoctorManagingCareTrackingController {
    private final IInitializeCareTracking initializeCareTracking;
    private final IGetCareTrackings getCareTracking;

    public DoctorManagingCareTrackingController(IInitializeCareTracking initializeCareTracking, IGetCareTrackings getCareTracking) {
        this.initializeCareTracking = initializeCareTracking;
        this.getCareTracking = getCareTracking;
    }

    @PostMapping("care-trackings")
    @ResponseStatus(HttpStatus.CREATED)
    public InitializeCareTrackingResponse initializeCareTracking(@Valid @RequestBody SaveCareTrackingRequest request) {
        return this.initializeCareTracking.execute(request);
    }

    @GetMapping("care-tracking")
    @ResponseStatus(HttpStatus.OK)
    public List<GetCareTrackingsResponse> getCareTrackings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return this.getCareTracking.execute(page, size);
    }

    @GetMapping("care-tracking/{id}")
    public GetCareTrackingsResponse getCareTrackingById(@PathVariable UUID id) {
        return this.getCareTracking.execute(id);
    }
}
