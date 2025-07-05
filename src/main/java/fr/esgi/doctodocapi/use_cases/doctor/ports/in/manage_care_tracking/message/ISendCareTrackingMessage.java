package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.message.SendMessageRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;

import java.util.UUID;

public interface ISendCareTrackingMessage {
    CareTrackingMessageResponse execute(UUID careTrackingId, SendMessageRequest request);
}
