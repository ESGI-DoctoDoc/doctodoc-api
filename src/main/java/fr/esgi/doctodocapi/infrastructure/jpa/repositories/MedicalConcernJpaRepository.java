package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalConcernJpaRepository extends JpaRepository<MedicalConcernEntity, UUID> {
    List<MedicalConcernEntity> findAllByDoctor_IdAndDeletedAtIsNull(UUID id);
    @Query("""
    SELECT mc FROM MedicalConcernEntity mc
    WHERE mc.id IN :ids AND mc.deletedAt IS NULL
""")
    List<MedicalConcernEntity> findAllByIdInAndDeletedAtIsNull(@Param("ids") List<UUID> ids);

    Optional<MedicalConcernEntity> findByIdAndDoctor_IdAndDeletedAtIsNull(UUID id, UUID doctorId);

    MedicalConcernEntity findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByIdAndDeletedAtIsNotNull(UUID id);

    @Query("SELECT CASE WHEN COUNT(mc) > 0 THEN true ELSE false END FROM MedicalConcernEntity mc WHERE LOWER(CAST(UNACCENT(mc.name) AS string)) = LOWER(CAST(UNACCENT(:name) AS string)) AND mc.doctor.id = :doctorId AND mc.deletedAt IS NULL")
    boolean existsByNameIgnoreCaseAndDoctor_Id(String name, UUID doctorId);

    @Query("""
        SELECT mc FROM MedicalConcernEntity mc
        WHERE mc.doctor.id = :doctorId
        AND LOWER(mc.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    Page<MedicalConcernEntity> searchByDoctorIdAndNameContainingIgnoreCase(
            @Param("doctorId") UUID doctorId,
            @Param("name") String concernName,
            Pageable pageable
    );

    @Query("""
        SELECT mc FROM MedicalConcernEntity mc
        LEFT JOIN FETCH mc.questions
        WHERE mc.id IN :ids
    """)
    List<MedicalConcernEntity> findAllWithQuestionsByIds(@Param("ids") List<UUID> ids);
}
