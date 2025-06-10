package fr.esgi.doctodocapi.infrastructure.services.patient;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.PatientMapper;
import fr.esgi.doctodocapi.domain.entities.patient.Patient;
import fr.esgi.doctodocapi.domain.entities.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.domain.entities.patient.PatientRepository;
import fr.esgi.doctodocapi.domain.entities.user.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the PatientRepository interface.
 * This service provides methods to manage patient data, including retrieving, saving,
 * and checking existence of patient information.
 */
@Service
public class PatientRepositoryImpl implements PatientRepository {
    /**
     * Repository for accessing patient data in the database.
     */
    private final PatientJpaRepository patientJpaRepository;

    /**
     * Mapper for converting between patient domain objects and entities.
     */
    private final PatientMapper patientMapper;

    private final EntityManager entityManager;


    /**
     * Constructs a PatientRepositoryImpl with the required repositories and mapper.
     *
     * @param patientJpaRepository Repository for patient data access
     * @param patientMapper        Mapper for patient domain objects and entities
     */
    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository, PatientMapper patientMapper, EntityManager entityManager) {
        this.patientJpaRepository = patientJpaRepository;
        this.patientMapper = patientMapper;
        this.entityManager = entityManager;
    }

    /**
     * Retrieves a patient by their associated user ID.
     * This method only returns the main account for a user.
     *
     * @param userId The unique identifier of the user associated with the patient
     * @return An Optional containing the patient if found, or an empty Optional if not found
     */
    @Override
    public Optional<Patient> getByUserId(UUID userId) {
        Optional<PatientEntity> patient = this.patientJpaRepository.findByUser_IdAndIsMainAccount(userId, true);

        if (patient.isPresent()) {
            PatientEntity entity = patient.get();
            return Optional.of(this.patientMapper.toDomain(entity));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Saves a patient to the database.
     * This method retrieves the associated user entity and doctor entity (if applicable) before saving the patient.
     *
     * @param patient The patient to save
     * @throws UserNotFoundException If the user or doctor associated with the patient does not exist
     */
    @Override
    public Patient save(Patient patient) {
        UserEntity user = this.entityManager.getReference(UserEntity.class, patient.getUserId());

        DoctorEntity doctor = null;
        if (patient.getDoctor() != null) {
            doctor = this.entityManager.getReference(DoctorEntity.class, patient.getDoctor().getId());
        }

        PatientEntity entityToSaved = this.patientMapper.toEntity(patient, user, doctor);

        PatientEntity patientSaved = this.patientJpaRepository.save(entityToSaved);
        return this.patientMapper.toDomain(patientSaved);
    }

    /**
     * Checks if a main account exists for the specified user ID.
     *
     * @param userId The unique identifier of the user to check
     * @return true if a main account exists for the specified user ID, false otherwise
     */
    @Override
    public boolean isExistMainAccount(UUID userId) {
        return this.patientJpaRepository.existsByUser_IdAndIsMainAccount(userId, true);
    }

    /**
     * Retrieves all close members (non-main accounts) associated with a specific user ID.
     *
     * @param id The unique identifier of the user whose close members are to be retrieved
     * @return A list of patients representing the close members of the specified user
     */
    @Override
    public List<Patient> getCloseMembers(UUID id) {
        List<PatientEntity> closeMembers = this.patientJpaRepository.findAllByUser_IdAndIsMainAccount(id, false);
        return closeMembers.stream().map(this.patientMapper::toDomain).toList();
    }

    /**
     * Retrieves a patient by their unique identifier.
     *
     * @param id The unique identifier of the patient to retrieve
     * @return The patient with the specified ID
     * @throws PatientNotFoundException If no patient with the specified ID exists
     */
    @Override
    public Patient getById(UUID id) {
        PatientEntity entity = this.patientJpaRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        return this.patientMapper.toDomain(entity);
    }

    /**
     * Updates an existing patient in the database.
     * Retrieves associated user and doctor references,
     * maps the domain model to an entity, persists it, and returns the updated domain model.
     *
     * @param patient the Patient domain object to update
     * @return the updated Patient domain object
     */
    @Override
    public Patient update(Patient patient) {
        UserEntity user = this.entityManager.getReference(UserEntity.class, patient.getUserId());

        DoctorEntity doctor = null;
        if (patient.getDoctor() != null) {
            doctor = this.entityManager.getReference(DoctorEntity.class, patient.getDoctor().getId());
        }

        PatientEntity entityToSaved = this.patientMapper.toEntity(patient, user, doctor);
        entityToSaved.setId(patient.getId());
        PatientEntity patientSaved = this.patientJpaRepository.save(entityToSaved);
        return this.patientMapper.toDomain(patientSaved);
    }

    @Override
    public void delete(UUID id) {
        PatientEntity entity = this.patientJpaRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        entity.setDeletedAt(LocalDateTime.now());
        this.patientJpaRepository.save(entity);
    }

}
