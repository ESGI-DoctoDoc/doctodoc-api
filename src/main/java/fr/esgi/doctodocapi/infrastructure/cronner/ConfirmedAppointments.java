package fr.esgi.doctodocapi.infrastructure.cronner;


import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AppointmentJpaRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ConfirmedAppointments {
    private static final Logger logger = LoggerFactory.getLogger(ConfirmedAppointments.class);

    private final AppointmentJpaRepository appointmentJpaRepository;

    public ConfirmedAppointments(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Scheduled(cron = "0 0 3 * * *") // todo get the time
    public void execute() {
        try {
            String oldStatus = AppointmentStatus.CONFIRMED.getValue();
            String status = AppointmentStatus.COMPLETED.getValue();

            LocalDate now = LocalDate.now();

            List<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllByStatusAndDateBefore(oldStatus, now);
            appointments.forEach(appointment -> {
                appointment.setStatus(status);
                this.appointmentJpaRepository.save(appointment);
            });

            logger.info("SUCCESS - Update past appointments in confirmed status");
        } catch (Exception e) {
            logger.error("ERROR - Cannot update past appointments in confirmed status cause of {}", e.getMessage());
        }
    }
}
