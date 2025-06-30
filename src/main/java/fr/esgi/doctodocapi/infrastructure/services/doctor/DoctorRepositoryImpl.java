package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentTracesJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceFacadeMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the DoctorRepository interface.
 * This service provides methods to manage doctor data, including retrieving, checking existence,
 * and saving doctor information.
 */
@Repository
public class DoctorRepositoryImpl implements DoctorRepository {
    /**
     * Repository for accessing doctor data in the database.
     */
    private final DoctorJpaRepository doctorJpaRepository;

    /**
     * Mapper for converting between doctor domain objects and entities.
     */
    private final DoctorMapper doctorMapper;


    /**
     * Mapper facade for converting between doctor domain objects and entities,
     * incluant mapping des slots et absences.
     */
    private final DoctorFacadeMapper doctorFacadeMapper;

    /**
     * Repository for accessing user data in the database.
     */
    private final UserJpaRepository userJpaRepository;

    private final DocumentMapper documentMapper;
    private final DocumentJpaRepository documentJpaRepository;
    private final DocumentTraceFacadeMapper documentTraceFacadeMapper;
    private final DocumentTracesJpaRepository documentTracesJpaRepository;
    private final EntityManager entityManager;

    /**
     * Constructs a DoctorRepositoryImpl with the required repositories and mapper.
     *
     * @param doctorJpaRepository Repository for doctor data access
     * @param doctorMapper        Mapper for doctor domain objects and entities
     * @param userJpaRepository   Repository for user data access
     */
    public DoctorRepositoryImpl(DoctorJpaRepository doctorJpaRepository, DoctorMapper doctorMapper, DoctorFacadeMapper doctorFacadeMapper, UserJpaRepository userJpaRepository, DocumentMapper documentMapper, DocumentJpaRepository documentJpaRepository, DocumentTraceFacadeMapper documentTraceFacadeMapper, DocumentTracesJpaRepository documentTracesJpaRepository, EntityManager entityManager) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.doctorMapper = doctorMapper;
        this.doctorFacadeMapper = doctorFacadeMapper;
        this.userJpaRepository = userJpaRepository;
        this.documentMapper = documentMapper;
        this.documentJpaRepository = documentJpaRepository;
        this.documentTraceFacadeMapper = documentTraceFacadeMapper;
        this.documentTracesJpaRepository = documentTracesJpaRepository;
        this.entityManager = entityManager;
    }


    /**
     * Retrieves a doctor by their unique identifier.
     *
     * @param id The unique identifier of the doctor to retrieve
     * @return The doctor with the specified ID
     * @throws DoctorNotFoundException If no doctor with the specified ID exists
     */
    @Override
    public Doctor getById(UUID id) throws DoctorNotFoundException {
        DoctorEntity entity = this.doctorJpaRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
        return this.doctorFacadeMapper.mapDoctorToDomain(entity);
    }

    /**
     * Checks if a doctor exists with the specified ID.
     *
     * @param doctorId The unique identifier of the doctor to check
     * @return true if a doctor with the specified ID exists, false otherwise
     */
    @Override
    public boolean isExistsById(UUID doctorId) {
        return this.doctorJpaRepository.existsById(doctorId);
    }

    /**
     * Retrieves a doctor by their associated user ID.
     *
     * @param userId The unique identifier of the user associated with the doctor
     * @return An Optional containing the doctor if found, or an empty Optional if not found
     */
    @Override
    public Optional<Doctor> getByUserId(UUID userId) {
        Optional<DoctorEntity> doctor = this.doctorJpaRepository.findByUser_Id(userId);

        if (doctor.isPresent()) {
            DoctorEntity entity = doctor.get();
            return Optional.of(this.doctorFacadeMapper.mapDoctorToDomain(entity));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Doctor> searchDoctors(String name, String speciality, List<String> languages, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String[] array = (languages != null) ? languages.toArray(new String[0]) : new String[0];

        Page<DoctorEntity> doctors = this.doctorJpaRepository.searchDoctors(name, speciality, array, pageable);
        return doctors.stream().map(this.doctorFacadeMapper::mapDoctorToDomain).toList();
    }

    /**
     * Saves a doctor to the database.
     * This method retrieves the associated user entity before saving the doctor.
     *
     * @param doctor The doctor to save
     * @throws UserNotFoundException If the user associated with the doctor does not exist
     */
    @Override
    public void save(Doctor doctor) {
        UserEntity user = this.userJpaRepository.findById(doctor.getUserId()).orElseThrow(UserNotFoundException::new);
        DoctorEntity entityToSaved;

        if (doctorJpaRepository.existsById(doctor.getId())) {
            entityToSaved = entityManager.getReference(DoctorEntity.class, doctor.getId());
        } else {
            entityToSaved = this.doctorMapper.toEntity(doctor, user);
        }

        DoctorEntity savedEntity = this.doctorJpaRepository.save(entityToSaved);
        saveDocuments(doctor.getProfessionalInformations().getDoctorDocuments(), savedEntity);
    }

    private void saveDocuments(List<Document> documents, DoctorEntity doctorEntity) {
        if (!documents.isEmpty()) {
            documents
                    .forEach(document -> {
                        DocumentEntity entity = this.documentMapper.toEntity(document, null, null, doctorEntity);
                        if (documentJpaRepository.existsById(document.getId())) {
                            entity.setId(document.getId());
                        }

                        this.documentJpaRepository.save(entity);
                        saveTraces(document);
                    });
        }
    }

    private void saveTraces(Document document) {
        List<DocumentTracesEntity> traces = document.getTraces().stream().map(trace -> this.documentTraceFacadeMapper.toEntity(document.getId(), trace)).toList();
        this.documentTracesJpaRepository.saveAll(traces);
    }

    /**
     * Finds a doctor by their user ID.
     * Unlike getByUserId, this method throws an exception if the doctor is not found.
     *
     * @param doctorId The unique identifier of the doctor to find
     * @return The doctor with the specified ID
     * @throws DoctorNotFoundException If no doctor with the specified ID exists
     */
    @Override
    public Doctor findDoctorByUserId(UUID doctorId) {
        DoctorEntity entity = this.doctorJpaRepository.findById(doctorId)
                .orElseThrow(DoctorNotFoundException::new);
        return doctorFacadeMapper.mapDoctorToDomain(entity);
    }

    @Override
    public List<Doctor> findAllForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorEntity> pageResult = doctorJpaRepository.findAll(pageable);
        return pageResult.getContent().stream()
                .map(doctorFacadeMapper::mapDoctorToDomain)
                .toList();
    }
}
