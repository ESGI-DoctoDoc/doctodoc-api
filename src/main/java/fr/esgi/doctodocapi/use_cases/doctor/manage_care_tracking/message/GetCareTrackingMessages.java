package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.message;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMessageResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.IGetCareTrackingMessages;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetCareTrackingMessages implements IGetCareTrackingMessages {

    private final MessageRepository messageRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final CareTrackingMessageResponseMapper messageResponseMapper;
    private final FileStorageService fileStorageService;

    public GetCareTrackingMessages(
            MessageRepository messageRepository,
            CareTrackingRepository careTrackingRepository,
            DoctorRepository doctorRepository,
            UserRepository userRepository,
            GetCurrentUserContext getCurrentUserContext,
            CareTrackingMessageResponseMapper messageResponseMapper,
            FileStorageService fileStorageService
    ) {
        this.messageRepository = messageRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.messageResponseMapper = messageResponseMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<CareTrackingMessageResponse> execute(UUID careTrackingId, LocalDateTime cursorSentAt, UUID cursorId) {
        try {
            User currentUser = userRepository.findByEmail(getCurrentUserContext.getUsername());
            Doctor doctor = doctorRepository.findDoctorByUserId(currentUser.getId());
            CareTracking careTracking = careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);

            List<Message> messages;
            if (cursorSentAt != null && cursorId != null) {
                messages = messageRepository.findPreviousMessages(careTrackingId, cursorSentAt, cursorId);
            } else {
                messages = messageRepository.findLatestMessages(careTrackingId);
            }

            Map<UUID, String> fileUrls = careTracking.getDocuments().stream()
                    .collect(Collectors.toMap(
                            doc -> doc.getDocument().getId(),
                            doc -> fileStorageService.getFile(doc.getDocument().getPath())
                    ));

            return messages.stream()
                    .map(msg -> {
                        Doctor sender = doctorRepository.findDoctorByUserId(msg.getSenderId());
                        List<String> files = msg.getFiles() != null
                                ? msg.getFiles().stream()
                                .map(fileUrls::get)
                                .filter(Objects::nonNull)
                                .toList()
                                : List.of();
                        return messageResponseMapper.toResponse(msg, sender, files);
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}