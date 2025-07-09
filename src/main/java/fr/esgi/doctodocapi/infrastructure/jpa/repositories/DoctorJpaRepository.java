package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, UUID> {
    Optional<DoctorEntity> findById(UUID id);

    Optional<DoctorEntity> findByUser_Id(UUID userId);

    @Query(value = """
            SELECT d.*
                FROM doctors d
                JOIN specialities s ON d.speciality_id = s.speciality_id
                WHERE
                    (
                        :name IS NULL OR (
                            LOWER(d.first_name || ' ' || d.last_name) LIKE CONCAT('%', LOWER(:name), '%')
                            OR LOWER(d.last_name || ' ' || d.first_name) LIKE CONCAT('%', LOWER(:name), '%')
                        )
                    )
                    AND (:speciality IS NULL OR LOWER(s.name) = LOWER(:speciality))
                    AND (
                        cardinality(:languages) = 0
                        OR EXISTS (
                            SELECT 1
                            FROM unnest(d.languages) AS lang
                            WHERE EXISTS (
                                SELECT 1
                                FROM unnest(:languages) AS input_lang
                                WHERE LOWER(lang) = LOWER(input_lang)
                            )
                        )
                    )""", nativeQuery = true)
    Page<DoctorEntity> searchDoctors(
            @Param("name") String name,
            @Param("speciality") String speciality,
            @Param("languages") String[] languages,
            Pageable pageable
    );

    @Query(value = """
            SELECT d.*
            FROM doctors d
            JOIN specialities s ON d.speciality_id = s.speciality_id
            JOIN (
                SELECT DISTINCT ON (doctor_id) *
                FROM doctor_subscriptions
                ORDER BY doctor_id, start_date DESC
            ) sub ON sub.doctor_id = d.doctor_id
            JOIN doctor_invoices i ON i.subscription_id = sub.subscription_id AND i.state = 'PAID'
            WHERE (sub.end_date IS NULL OR sub.end_date::date >= CURRENT_DATE)
              AND (
                  :name IS NULL OR (
                      LOWER(d.first_name || ' ' || d.last_name) LIKE CONCAT('%', LOWER(:name), '%')
                      OR LOWER(d.last_name || ' ' || d.first_name) LIKE CONCAT('%', LOWER(:name), '%')
                  )
              )
              AND (:speciality IS NULL OR LOWER(s.name) = LOWER(:speciality))
              AND (
                  cardinality(:languages) = 0
                  OR EXISTS (
                      SELECT 1
                      FROM unnest(d.languages) AS lang
                      WHERE EXISTS (
                          SELECT 1
                          FROM unnest(:languages) AS input_lang
                          WHERE LOWER(lang) = LOWER(input_lang)
                      )
                  )
              )
            """, nativeQuery = true)
    Page<DoctorEntity> searchValidDoctors(
            @Param("name") String name,
            @Param("speciality") String speciality,
            @Param("languages") String[] languages,
            Pageable pageable
    );

    @Query("SELECT d FROM DoctorEntity d WHERE LOWER(CONCAT(d.firstName, ' ', d.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<DoctorEntity> searchByDoctorName(@Param("name") String name, Pageable pageable);
}
