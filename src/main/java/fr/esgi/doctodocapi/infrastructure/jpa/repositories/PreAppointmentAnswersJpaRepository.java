package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PreAppointmentAnswersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreAppointmentAnswersJpaRepository extends JpaRepository<PreAppointmentAnswersEntity, UUID> {
}
