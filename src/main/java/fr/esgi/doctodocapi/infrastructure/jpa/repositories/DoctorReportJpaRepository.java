package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorReportJpaRepository extends JpaRepository<DoctorReportEntity, UUID> {
}
