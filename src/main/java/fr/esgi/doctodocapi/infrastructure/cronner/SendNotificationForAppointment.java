package fr.esgi.doctodocapi.infrastructure.cronner;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AppointmentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SendNotificationForAppointment {
    private static final Logger logger = LoggerFactory.getLogger(SendNotificationForAppointment.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH);


    private final AppointmentJpaRepository appointmentJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final NotificationPushService notificationPushService;

    public SendNotificationForAppointment(AppointmentJpaRepository appointmentJpaRepository, PatientJpaRepository patientJpaRepository, NotificationPushService notificationPushService) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.notificationPushService = notificationPushService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void execute() {
        try {
            List<AppointmentEntity> appointmentsToNotified = new ArrayList<>();

            List<AppointmentEntity> appointmentsInOneHour = getAppointmentsIn(60);
            List<AppointmentEntity> appointmentsInFifteenMinutes = getAppointmentsIn(15);

            appointmentsToNotified.addAll(appointmentsInFifteenMinutes);
            appointmentsToNotified.addAll(appointmentsInOneHour);

            sendAppointmentNotification(appointmentsToNotified);

            logger.info("SUCCESS - Sent reminders appointments {}", appointmentsToNotified.size());

        } catch (Exception e) {
            logger.error("ERROR - Cannot send notifications {}", e.getMessage());
        }
    }

    private List<AppointmentEntity> getAppointmentsIn(int minutes) {
        String status = AppointmentStatus.CONFIRMED.getValue();
        LocalDateTime targetTime = LocalDateTime.now().plusMinutes(minutes).withNano(0);
        return this.appointmentJpaRepository.findAllByStatusAndDateAndStartHour(
                status,
                targetTime.toLocalDate(),
                targetTime.toLocalTime()
        );
    }

    private void sendAppointmentNotification(List<AppointmentEntity> appointments) {
        appointments.forEach(appointment -> {
            PatientEntity patient = appointment.getPatient();
            PatientEntity notificationRecipient = patient;

            if (!patient.getIsMainAccount()) {
                notificationRecipient = this.patientJpaRepository
                        .findByUser_IdAndIsMainAccount(patient.getUser().getId(), true)
                        .orElseThrow(() -> new CannotFindMainAccountPatient(patient.getUser().getId()));
            }

            String doctorFullName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
                String formattedDate = appointment.getStartHour().format(TIME_FORMATTER);
                NotificationMessage message = new NotificationMessage(
                        notificationRecipient.getUser().getId(),
                        "Vous avez pris un rdv avec le docteur " + doctorFullName + " aujourd'hui à  " + formattedDate,
                        "Rappel de RDV",
                        Map.of("appointment_id", appointment.getId().toString())
                );
            this.notificationPushService.send(message);

        });
    }
}
