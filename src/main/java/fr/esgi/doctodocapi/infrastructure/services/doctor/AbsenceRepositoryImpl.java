package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AbsenceJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceMapper;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import org.springframework.stereotype.Service;

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

    public AbsenceRepositoryImpl(AbsenceJpaRepository absenceJpaRepository, DoctorJpaRepository doctorJpaRepository, AbsenceMapper absenceMapper) {
        this.absenceJpaRepository = absenceJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.absenceMapper = absenceMapper;
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
    public Absence save(Absence absence) {
        DoctorEntity doctorEntity = this.doctorJpaRepository.findById(absence.getDoctorId()).orElseThrow(DoctorNotFoundException::new);
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
        List<AbsenceEntity> absenceEntities = this.absenceJpaRepository.findAllByDoctorId(doctorEntity.getId());

        return absenceEntities.stream().map(absenceMapper::toDomain).toList();
    }
}
