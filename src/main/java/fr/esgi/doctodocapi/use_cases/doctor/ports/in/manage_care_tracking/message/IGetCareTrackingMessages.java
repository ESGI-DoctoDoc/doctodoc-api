package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;

import java.util.List;
import java.util.UUID;

public interface IGetCareTrackingMessages {
    List<CareTrackingMessageResponse> execute(UUID careTrackingId);
}
