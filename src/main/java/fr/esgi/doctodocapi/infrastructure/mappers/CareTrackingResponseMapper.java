package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.CareTrackingPatientInfo;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CareTrackingResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<GetCareTrackingsResponse> toResponseList(List<CareTracking> careTrackings) {
        return careTrackings.stream()
                .map(this::toResponse)
                .toList();
    }

    public GetCareTrackingsResponse toResponse(CareTracking careTracking) {
        return new GetCareTrackingsResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getCreatedAt().format(DATE_FORMATTER),
                new CareTrackingPatientInfo(
                        careTracking.getPatient().getId(),
                        careTracking.getPatient().getFirstName(),
                        careTracking.getPatient().getLastName(),
                        careTracking.getPatient().getEmail().getValue(),
                        careTracking.getPatient().getPhoneNumber().getValue()
                )
        );
    }
}