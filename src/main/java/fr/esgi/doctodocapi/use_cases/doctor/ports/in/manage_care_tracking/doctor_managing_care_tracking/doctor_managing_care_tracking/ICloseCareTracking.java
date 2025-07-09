package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.CloseCareTrackingResponse;

import java.util.UUID;

public interface ICloseCareTracking {
    CloseCareTrackingResponse execute(UUID careTrackingId);
}
