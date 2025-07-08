package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga;

import java.util.UUID;

public record CareTrackingMessageResponse(
        UUID id,
        SenderInfo sender,
        ContentInfo content,
        String sentAt
) {
}
