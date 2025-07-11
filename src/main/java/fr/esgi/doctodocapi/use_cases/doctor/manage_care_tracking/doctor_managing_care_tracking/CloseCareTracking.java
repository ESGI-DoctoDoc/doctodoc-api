package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.CloseCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.ICloseCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.UnauthorizedToCloseCareTrackingException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CloseCareTracking implements ICloseCareTracking {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPushService notificationPushService;


    public CloseCareTracking(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, NotificationRepository notificationRepository, NotificationPushService notificationPushService) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.notificationRepository = notificationRepository;
        this.notificationPushService = notificationPushService;
    }

    public CloseCareTrackingResponse execute(UUID careTrackingId) {
        String username = this.getCurrentUserContext.getUsername();
        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);

            if (!Objects.equals(doctor.getId(), careTracking.getCreatorId())) {
                throw new UnauthorizedToCloseCareTrackingException();
            }

            careTracking.close();

            UUID closedCareTracking = this.careTrackingRepository.close(careTracking);

            notifyDoctorOfCloseCareTracking(careTracking);
            notifyPatientOfCloseCareTracking(careTracking, doctor);
            return new CloseCareTrackingResponse(closedCareTracking);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private void notifyDoctorOfCloseCareTracking(CareTracking careTracking) {
        List<UUID> doctors = new ArrayList<>(careTracking.getDoctors());

        doctors.forEach(doctorId -> {
                    Notification notification = NotificationsType.careTrackingClose(doctorId, careTracking.getCaseName());
                    this.notificationRepository.save(notification);
                }
        );
    }

    private void notifyPatientOfCloseCareTracking(CareTracking careTracking, Doctor creator) {
        String doctorFullName = creator.getPersonalInformations().getFirstName() + " " + creator.getPersonalInformations().getLastName();
        NotificationMessage message = NotificationMessageType.closeCareTracking(
                careTracking.getPatient().getUserId(),
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getClosedAt(),
                doctorFullName
        );
        this.notificationPushService.send(message);
    }
}
