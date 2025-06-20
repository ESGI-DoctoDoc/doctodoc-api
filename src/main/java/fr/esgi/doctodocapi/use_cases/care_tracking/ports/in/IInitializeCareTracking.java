package fr.esgi.doctodocapi.use_cases.care_tracking.ports.in;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.InitializeCareTrackingResponse;

public interface IInitializeCareTracking {
    InitializeCareTrackingResponse execute(SaveCareTrackingRequest request);
}
