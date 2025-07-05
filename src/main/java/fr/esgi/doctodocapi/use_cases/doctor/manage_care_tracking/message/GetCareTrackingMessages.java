package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.message;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMessageResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.IGetCareTrackingMessages;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetCareTrackingMessages implements IGetCareTrackingMessages {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MessageRepository messageRepository;
    private final CareTrackingMessageResponseMapper careTrackingMessageResponseMapper;

    public GetCareTrackingMessages(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, MessageRepository messageRepository, CareTrackingMessageResponseMapper careTrackingMessageResponseMapper) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.messageRepository = messageRepository;
        this.careTrackingMessageResponseMapper = careTrackingMessageResponseMapper;
    }

    public List<CareTrackingMessageResponse> execute(UUID careTrackingId) {
        try {
            List<Message> messages = this.messageRepository.findByCareTrackingId(careTrackingId);

            return messages.stream()
                    .map(message -> {
                        Doctor sender = this.doctorRepository.findDoctorByUserId(message.getSenderId());
                        return careTrackingMessageResponseMapper.toResponse(message, sender);
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
