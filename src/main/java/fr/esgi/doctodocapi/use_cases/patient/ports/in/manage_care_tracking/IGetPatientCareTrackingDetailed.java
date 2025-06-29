package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingDetailedResponse;

import java.util.UUID;

public interface IGetPatientCareTrackingDetailed {
    GetPatientCareTrackingDetailedResponse process(UUID id);
}
