package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.ContentInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.SenderInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CareTrackingMessageResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public CareTrackingMessageResponse toResponse(Message message, Doctor sender) {
        return new CareTrackingMessageResponse(
                message.getId(),
                new SenderInfo(
                        sender.getId(),
                        sender.getPersonalInformations().getFirstName() + " " + sender.getPersonalInformations().getLastName(),
                        sender.getPersonalInformations().getProfilePictureUrl()
                ),
                new ContentInfo(
                        message.getContent(),
                        List.of()
                ),
                formatDate(message.getSentAt())
        );
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }

}
