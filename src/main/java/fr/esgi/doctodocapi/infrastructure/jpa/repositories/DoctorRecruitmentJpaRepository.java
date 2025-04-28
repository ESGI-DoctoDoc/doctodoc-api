package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorRecruitmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRecruitmentJpaRepository extends JpaRepository<DoctorRecruitmentEntity, UUID> {
}
