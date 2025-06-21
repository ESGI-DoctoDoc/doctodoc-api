package fr.esgi.doctodocapi.use_cases.care_tracking.ports.in;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.GetCareTrackingsResponse;

import java.util.List;

public interface IGetCareTrackings {
    List<GetCareTrackingsResponse> execute(int page, int size);
}
