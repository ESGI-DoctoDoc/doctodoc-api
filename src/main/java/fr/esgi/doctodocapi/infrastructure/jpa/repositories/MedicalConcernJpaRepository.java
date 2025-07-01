package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalConcernJpaRepository extends JpaRepository<MedicalConcernEntity, UUID> {
    List<MedicalConcernEntity> findAllByDoctor_Id(UUID id);
    Optional<MedicalConcernEntity> findByIdAndDoctor_Id(UUID id, UUID doctorId);

    @Query("SELECT CASE WHEN COUNT(mc) > 0 THEN true ELSE false END FROM MedicalConcernEntity mc WHERE LOWER(CAST(UNACCENT(mc.name) AS string)) = LOWER(CAST(UNACCENT(:name) AS string)) AND mc.doctor.id = :doctorId")
    boolean existsByNameIgnoreCaseAndDoctor_Id(String name, UUID doctorId);
}
