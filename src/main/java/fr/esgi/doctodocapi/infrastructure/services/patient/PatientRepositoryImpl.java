package fr.esgi.doctodocapi.infrastructure.services.patient;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.PatientMapper;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
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
     * Repository for accessing user data in the database.
     */
    private final UserJpaRepository userJpaRepository;

    /**
     * Repository for accessing doctor data in the database.
     */
    private final DoctorJpaRepository doctorJpaRepository;

    /**
     * Mapper for converting between patient domain objects and entities.
     */
    private final PatientMapper patientMapper;

    /**
     * Constructs a PatientRepositoryImpl with the required repositories and mapper.
     *
     * @param patientJpaRepository Repository for patient data access
     * @param userJpaRepository    Repository for user data access
     * @param doctorJpaRepository  Repository for doctor data access
     * @param patientMapper        Mapper for patient domain objects and entities
     */
    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository, UserJpaRepository userJpaRepository,
                                 DoctorJpaRepository doctorJpaRepository, PatientMapper patientMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.patientMapper = patientMapper;
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
        UserEntity user = this.userJpaRepository.findById(patient.getUserId()).orElseThrow(UserNotFoundException::new);

        DoctorEntity doctor = null;
        if (patient.getDoctor() != null) {
            doctor =
                    this.doctorJpaRepository.findById(patient.getDoctor().getId()).orElseThrow(UserNotFoundException::new);
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

    @Override
    public Patient update(Patient patient) {
        UserEntity user = this.userJpaRepository.findById(patient.getUserId()).orElseThrow(UserNotFoundException::new);
        PatientEntity entityToSaved = this.patientMapper.toEntity(patient, user, null);
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
