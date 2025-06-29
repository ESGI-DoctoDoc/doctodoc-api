package fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.doctor_managing_care_tracking.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_managing_care_tracking.InitializeCareTrackingResponse;

public interface IInitializeCareTracking {
    InitializeCareTrackingResponse execute(SaveCareTrackingRequest request);
}
