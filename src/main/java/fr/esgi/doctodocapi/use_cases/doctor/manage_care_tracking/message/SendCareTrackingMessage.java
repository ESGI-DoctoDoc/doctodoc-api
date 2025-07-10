package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.message;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMessageResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.message.SendMessageRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.ISendCareTrackingMessage;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class SendCareTrackingMessage implements ISendCareTrackingMessage {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CareTrackingMessageResponseMapper careTrackingMessageResponseMapper;
    private final FileStorageService fileStorageService;
    private final NotificationRepository notificationRepository;

    public SendCareTrackingMessage(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate, CareTrackingMessageResponseMapper careTrackingMessageResponseMapper, FileStorageService fileStorageService, NotificationRepository notificationRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.careTrackingMessageResponseMapper = careTrackingMessageResponseMapper;
        this.fileStorageService = fileStorageService;
        this.notificationRepository = notificationRepository;
    }

    public CareTrackingMessageResponse execute(UUID careTrackingId, SendMessageRequest request) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());
            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);

            List<UUID> documentIds = null;
            List<String> fileUrls = List.of();
            if (request.files() != null && !request.files().isEmpty()) {
                documentIds = request.files().stream()
                        .map(UUID::fromString)
                        .toList();

                Map<UUID, String> documentIdToUrl = careTracking.getDocuments().stream()
                        .collect(Collectors.toMap(
                                doc -> doc.getDocument().getId(),
                                doc -> fileStorageService.getFile(doc.getDocument().getPath())
                        ));

                fileUrls = documentIds.stream()
                        .map(documentIdToUrl::get)
                        .toList();
            }

            String cleanedContent = request.content() != null ? request.content().replaceAll("^\\s+|\\s+$", "") : null;

            Message message = Message.create(doctor.getId(), careTracking.getId(), cleanedContent, documentIds);
            this.messageRepository.save(message);

            Doctor sender = this.doctorRepository.getById(message.getSenderId());
            CareTrackingMessageResponse response = this.careTrackingMessageResponseMapper.toResponse(message, sender, fileUrls);

            this.messagingTemplate.convertAndSend("/topic/" + careTrackingId, response);

            notifyDoctorOfNewMessages(careTracking, sender.getId());

            return response;

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", e.getMessage());
        }
    }

    private void notifyDoctorOfNewMessages(CareTracking careTracking, UUID sender) {
        List<UUID> doctors = new ArrayList<>(careTracking.getDoctors());
        doctors.add(careTracking.getCreatorId());

        doctors = doctors.stream().filter(id -> !Objects.equals(sender, id)).toList();

        doctors.forEach(doctorId -> {
                    Notification notification = NotificationsType.newMessageInCareTracking(doctorId, careTracking.getCaseName());
                    this.notificationRepository.save(notification);
                }
        );
    }
}