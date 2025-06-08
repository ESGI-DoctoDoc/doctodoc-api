package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.QuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.QuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MedicalConcernJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.QuestionMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalConcernMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the MedicalConcernRepository interface.
 * This service provides methods to manage medical concerns and related doctor questions,
 * allowing the application to retrieve information about medical conditions and their associated questionnaires.
 */
@Service
public class MedicalConcernRepositoryImpl implements MedicalConcernRepository {
    /**
     * Repository for accessing doctor question data in the database.
     */
    private final QuestionJpaRepository questionJpaRepository;

    /**
     * Repository for accessing medical concern data in the database.
     */
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    /**
     * Mapper for converting between doctor question domain objects and entities.
     */
    private final QuestionMapper questionMapper;

    /**
     * Mapper for converting between medical concern domain objects and entities.
     */
    private final MedicalConcernMapper medicalConcernMapper;

    /**
     * Repository for accessing doctor data in the database.
     */
    private final DoctorJpaRepository doctorJpaRepository;

    /**
     * Constructs a MedicalConcernRepositoryImpl with the required repositories and mappers.
     *
     * @param questionJpaRepository Repository for doctor question data access
     * @param doctorJpaRepository Repository for doctor data access
     * @param medicalConcernJpaRepository Repository for medical concern data access
     * @param questionMapper       Mapper for doctor question domain objects and entities
     * @param medicalConcernMapper        Mapper for medical concern domain objects and entities
     */
    public MedicalConcernRepositoryImpl(QuestionJpaRepository questionJpaRepository, MedicalConcernJpaRepository medicalConcernJpaRepository, QuestionMapper questionMapper, MedicalConcernMapper medicalConcernMapper, DoctorJpaRepository doctorJpaRepository) {
        this.questionJpaRepository = questionJpaRepository;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
        this.questionMapper = questionMapper;
        this.medicalConcernMapper = medicalConcernMapper;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    /**
     * Retrieves all medical concerns associated with a specific doctor.
     *
     * @param doctor The doctor whose medical concerns are to be retrieved
     * @return A list of medical concerns associated with the specified doctor
     */
    @Override
    public List<MedicalConcern> getMedicalConcerns(Doctor doctor) {
        List<MedicalConcernEntity> entities = this.medicalConcernJpaRepository.findAllByDoctor_Id(doctor.getId());
        return entities.stream().map(medicalConcernMapper::toDomain).toList();
    }

    /**
     * Retrieves all questions associated with a specific medical concern.
     *
     * @param medicalConcern The medical concern whose questions are to be retrieved
     * @return A list of questions associated with the specified medical concern
     */
    @Override
    public List<Question> getDoctorQuestions(MedicalConcern medicalConcern) {
        List<QuestionEntity> entities = this.questionJpaRepository.findAllByMedicalConcern_IdAndDeletedAtIsNull(medicalConcern.getId());
        return entities.stream().map(questionMapper::toDomain).toList();
    }

    /**
     * Retrieves a specific question by its unique identifier.
     *
     * @param uuid the ID of the question
     * @return the {@link Question} domain object
     * @throws QuestionNotFoundException if the question does not exist
     */
    @Override
    public Question getQuestionById(UUID uuid) throws QuestionNotFoundException {
        QuestionEntity entity = this.questionJpaRepository.findById(uuid).orElseThrow(QuestionNotFoundException::new);
        return this.questionMapper.toDomain(entity);
    }

    /**
     * Retrieves a medical concern by its unique identifier.
     *
     * @param medicalConcernId The unique identifier of the medical concern to retrieve
     * @return The medical concern with the specified ID
     * @throws MedicalConcernNotFoundException If no medical concern with the specified ID exists
     */
    @Override
    public MedicalConcern getById(UUID medicalConcernId) throws MedicalConcernNotFoundException {
        MedicalConcernEntity medicalConcern = this.medicalConcernJpaRepository.findById(medicalConcernId).orElseThrow(MedicalConcernNotFoundException::new);
        return this.medicalConcernMapper.toDomain(medicalConcern);
    }

    /**
     * Saves a medical concern to the database.
     * Converts the domain model to an entity, persists it, and returns the updated domain model.
     *
     * @param medicalConcern the {@link MedicalConcern} to be saved
     * @return the persisted {@link MedicalConcern} domain object
     * @throws UserNotFoundException if the associated doctor does not exist
     */
    @Override
    public MedicalConcern save(MedicalConcern medicalConcern) {
        DoctorEntity doctorEntity = this.doctorJpaRepository.findById(medicalConcern.getDoctorId()).orElseThrow(UserNotFoundException::new);
        doctorEntity.setId(medicalConcern.getDoctorId());
        MedicalConcernEntity medicalConcernEntity = this.medicalConcernMapper.toEntity(medicalConcern, doctorEntity);

        MedicalConcernEntity savedEntity = this.medicalConcernJpaRepository.save(medicalConcernEntity);

        return this.medicalConcernMapper.toDomain(savedEntity);
    }
}
