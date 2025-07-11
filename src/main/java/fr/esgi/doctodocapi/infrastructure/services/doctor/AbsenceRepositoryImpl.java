package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AbsenceJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceMapper;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceNotFoundException;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link AbsenceRepository} interface using JPA.
 * <p>
 * This class handles persistence operations related to doctor absences,
 * including saving a new absence and retrieving all absences for a doctor.
 */
@Service
public class AbsenceRepositoryImpl implements AbsenceRepository {
    private final AbsenceJpaRepository  absenceJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final AbsenceMapper absenceMapper;
    private final EntityManager entityManager;

    public AbsenceRepositoryImpl(AbsenceJpaRepository absenceJpaRepository, DoctorJpaRepository doctorJpaRepository, AbsenceMapper absenceMapper, EntityManager entityManager) {
        this.absenceJpaRepository = absenceJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.absenceMapper = absenceMapper;
        this.entityManager = entityManager;
    }

    /**
     * Saves a new absence in the database.
     * <p>
     * It first verifies the existence of the associated doctor, maps the domain object
     * to an entity, persists it, and then maps it back to the domain object.
     *
     * @param absence the absence to be saved
     * @return the saved absence
     * @throws DoctorNotFoundException if the doctor associated with the absence does not exist
     */
    @Override
    public Absence save(Absence absence, UUID doctorId) {
        DoctorEntity doctorEntity = entityManager.getReference(DoctorEntity.class, doctorId);
        AbsenceEntity absenceEntity = this.absenceMapper.toEntity(absence, doctorEntity);
        AbsenceEntity savedEntity = this.absenceJpaRepository.save(absenceEntity);

        return this.absenceMapper.toDomain(savedEntity);
    }

    /**
     * Retrieves all absences for a specific doctor.
     *
     * @param doctorId the UUID of the doctor
     * @return a list of absences for the doctor
     * @throws DoctorNotFoundException if the doctor does not exist
     */
    @Override
    public List<Absence> findAllByDoctorId(UUID doctorId) {
        DoctorEntity doctorEntity = this.doctorJpaRepository.findById(doctorId).orElseThrow(DoctorNotFoundException::new);
        List<AbsenceEntity> absenceEntities = this.absenceJpaRepository.findAllByDoctor_Id(doctorEntity.getId());

        return absenceEntities.stream().map(absenceMapper::toDomain).toList();
    }

    /**
     * Performs a logical (soft) deletion of an absence by marking its `deletedAt` field.
     *
     * @param absenceId the UUID of the absence to be deleted
     * @throws AbsenceNotFoundException if no matching absence is found
     */
    @Override
    public void delete(UUID absenceId) {
        AbsenceEntity absenceEntity = this.absenceJpaRepository.findById(absenceId).orElseThrow(AbsenceNotFoundException::new);
        absenceEntity.setDeletedAt(LocalDate.now());
        this.absenceJpaRepository.save(absenceEntity);
    }

    /**
     * Retrieves a specific {@link Absence} by its ID.
     *
     * @param absenceId the UUID of the absence to retrieve
     * @return the found absence entity mapped to the domain model
     * @throws AbsenceNotFoundException if no valid (non-deleted) absence is found
     */
    @Override
    public Absence findById(UUID absenceId) {
        return this.absenceJpaRepository.findById(absenceId)
                .map(absenceMapper::toDomain)
                .orElseThrow(AbsenceNotFoundException::new);
    }

    @Override
    public List<Absence> findAllByDoctorIdAndDate(UUID doctorId, LocalDate date) {
        List<AbsenceEntity> absenceEntities = this.absenceJpaRepository.findAllByDoctor_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(doctorId, date, date);
        return absenceEntities.stream().map(absenceMapper::toDomain).toList();
    }

    @Override
    public List<Absence> findAllByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<AbsenceEntity> pageResult = this.absenceJpaRepository.findAllByDoctor_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                doctorId,
                endDate,
                startDate,
                pageable
        );

        return pageResult.getContent().stream().map(absenceMapper::toDomain).toList();
    }

    @Override
    public List<Absence> findAllByDoctorIdAndStartDateAfterNow(UUID doctorId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AbsenceEntity> pageResult = this.absenceJpaRepository.findAllByDoctor_IdAndStartDateGreaterThanEqual(doctorId, date, pageable);

        return pageResult.getContent().stream().map(absenceMapper::toDomain).toList();
    }

    @Override
    public Absence update(Absence absence) {
        AbsenceEntity entity = this.entityManager.find(AbsenceEntity.class, absence.getId());

        entity.setDescription(absence.getDescription());
        entity.setStartDate(absence.getAbsenceRange().getDateRange().getStart());
        entity.setEndDate(absence.getAbsenceRange().getDateRange().getEnd());
        entity.setStartHour(absence.getAbsenceRange().getHoursRange().getStart());
        entity.setEndHour(absence.getAbsenceRange().getHoursRange().getEnd());

        AbsenceEntity savedEntity = this.absenceJpaRepository.save(entity);
        return this.absenceMapper.toDomain(savedEntity);
    }
}
