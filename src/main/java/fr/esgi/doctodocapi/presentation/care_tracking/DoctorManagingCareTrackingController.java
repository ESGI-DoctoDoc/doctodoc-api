package fr.esgi.doctodocapi.presentation.care_tracking;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.InitializeCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.IGetCareTrackings;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.IInitializeCareTracking;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
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
    public List<GetCareTrackingsResponse> getCareTrackings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getCareTracking.execute(page, size);
    }
}
