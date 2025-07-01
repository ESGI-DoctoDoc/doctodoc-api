package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentJpaRepository extends JpaRepository<DocumentEntity, UUID> {
    Page<DocumentEntity> getAllByMedicalRecord_PatientIdOrderByUploadedAtDesc(UUID medicalRecordPatientId, Pageable pageable);

    Page<DocumentEntity> getAllByMedicalRecord_PatientIdAndTypeContainsIgnoreCaseOrderByUploadedAtDesc(UUID medicalRecordPatientId, String type, Pageable pageable);
    List<DocumentEntity> findAllByUploadedBy(UUID uploadedBy);
}
