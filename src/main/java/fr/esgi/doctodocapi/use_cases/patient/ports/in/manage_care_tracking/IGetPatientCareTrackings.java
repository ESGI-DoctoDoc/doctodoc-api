package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;

import java.util.List;

public interface IGetPatientCareTrackings {
    List<GetPatientCareTrackingResponse> process(int page, int size);

}
