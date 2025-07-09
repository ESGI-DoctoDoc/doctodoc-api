package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking.UpdateCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCareTrackingResponse;

import java.util.UUID;

public interface IUpdateCareTracking {
    GetCareTrackingResponse execute(UUID careTrackingId, UpdateCareTrackingRequest request);
}
