package fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.doctor_managing_care_tracking.InitializeCareTrackingResponse;

public interface IInitializeCareTracking {
    InitializeCareTrackingResponse execute(SaveCareTrackingRequest request);
}
