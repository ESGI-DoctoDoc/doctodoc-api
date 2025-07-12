package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.ContentInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.SenderInfo;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CareTrackingMessageResponseMapper {
    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public CareTrackingMessageResponseMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public CareTrackingMessageResponse toResponse(Message message, Doctor sender, List<String> fileUrls) {
        return new CareTrackingMessageResponse(
                message.getId(),
                new SenderInfo(
                        sender.getId(),
                        sender.getPersonalInformations().getFirstName() + " " + sender.getPersonalInformations().getLastName(),
                        this.getDoctorProfileUrl.getUrl(sender.getPersonalInformations().getProfilePictureUrl())
                ),
                new ContentInfo(
                        message.getContent(),
                        fileUrls
                ),
                message.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    public CareTrackingMessageResponse toResponse(Message message, Doctor sender) {
        return toResponse(message, sender, List.of());
    }
}