package fr.esgi.doctodocapi.infrastructure.cronner;


import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AppointmentJpaRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DeleteAppointmentsLocked {
    private static final Logger logger = LoggerFactory.getLogger(DeleteAppointmentsLocked.class);

    private final AppointmentJpaRepository appointmentJpaRepository;

    public DeleteAppointmentsLocked(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Scheduled(cron = "0 */15 * * * *") // todo get the time
    @Transactional
    public void execute() {
        try {
            String status = AppointmentStatus.LOCKED.getValue();
            LocalDateTime nowMinusFiveMinutes = LocalDateTime.now().minusMinutes(5);
            this.appointmentJpaRepository.deleteAllByStatusAndLockedAtBefore(status, nowMinusFiveMinutes);
            logger.info("SUCCESS - Deleting appointments in locked status");
        } catch (Exception e) {
            logger.error("ERROR - Cannot delete locked appointments cause of {}", e.getMessage());
        }
    }
}
