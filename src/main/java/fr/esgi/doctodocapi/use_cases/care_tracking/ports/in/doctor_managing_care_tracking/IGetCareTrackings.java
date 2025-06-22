package fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.doctor_managing_care_tracking.GetCareTrackingsResponse;

import java.util.List;

public interface IGetCareTrackings {
    List<GetCareTrackingsResponse> execute(int page, int size);
}
