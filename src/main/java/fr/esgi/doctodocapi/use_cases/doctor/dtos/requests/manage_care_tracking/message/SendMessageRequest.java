package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.message;

import java.util.List;

public record SendMessageRequest(
        String content,
        List<String> files
        ) {
}
