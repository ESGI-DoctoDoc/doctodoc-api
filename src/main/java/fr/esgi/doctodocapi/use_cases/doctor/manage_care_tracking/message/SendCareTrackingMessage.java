package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.message;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMessageResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.message.SendMessageRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.ISendCareTrackingMessage;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

public class SendCareTrackingMessage implements ISendCareTrackingMessage {

    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CareTrackingMessageResponseMapper careTrackingMessageResponseMapper;

    public SendCareTrackingMessage(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate, CareTrackingMessageResponseMapper careTrackingMessageResponseMapper) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.careTrackingMessageResponseMapper = careTrackingMessageResponseMapper;
    }

    public CareTrackingMessageResponse execute(UUID careTrackingId, SendMessageRequest request) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctorId(careTrackingId, doctor);

            Message message = Message.create(doctor.getId(), careTracking.getId(), request.content());
            this.messageRepository.save(message);

            Doctor sender = this.doctorRepository.getById(message.getSenderId());

            CareTrackingMessageResponse response = this.careTrackingMessageResponseMapper.toResponse(message, sender);

            this.messagingTemplate.convertAndSend("/topic/" + careTrackingId, response);

            return response;

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
