package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga;

import java.util.List;

public record ContentInfo(
        String text,
        List<String> files
) {
}
