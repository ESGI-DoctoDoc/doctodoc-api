package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SpecialityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpecialityJpaRepository extends JpaRepository<SpecialityEntity, UUID> {
    Optional<SpecialityEntity> findByName(String name);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SpecialityEntity s WHERE LOWER(CAST(UNACCENT(s.name) AS string)) = LOWER(CAST(UNACCENT(:name) AS string))")
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT s FROM SpecialityEntity s WHERE LOWER(CAST(UNACCENT(s.name) AS string)) LIKE LOWER(CAST(UNACCENT(CONCAT('%', :name, '%')) AS string))")
    Page<SpecialityEntity> searchByName(@Param("name") String name, Pageable pageable);
}
