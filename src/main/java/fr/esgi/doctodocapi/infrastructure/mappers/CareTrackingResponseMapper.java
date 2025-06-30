package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.doctor_managing_care_tracking.CareTrackingPatientInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.doctor_managing_care_tracking.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
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