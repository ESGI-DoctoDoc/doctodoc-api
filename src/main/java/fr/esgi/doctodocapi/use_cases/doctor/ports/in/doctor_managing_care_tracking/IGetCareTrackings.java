package fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.doctor_managing_care_tracking.GetCareTrackingsResponse;

import java.util.List;

public interface IGetCareTrackings {
    List<GetCareTrackingsResponse> execute(int page, int size);
}
