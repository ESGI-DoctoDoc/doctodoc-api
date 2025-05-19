package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorQuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MedicalConcernJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorQuestionsMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalConcernMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
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
    private final DoctorQuestionJpaRepository doctorQuestionJpaRepository;

    /**
     * Repository for accessing medical concern data in the database.
     */
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    /**
     * Mapper for converting between doctor question domain objects and entities.
     */
    private final DoctorQuestionsMapper doctorQuestionsMapper;

    /**
     * Mapper for converting between medical concern domain objects and entities.
     */
    private final MedicalConcernMapper medicalConcernMapper;

    /**
     * Constructs a MedicalConcernRepositoryImpl with the required repositories and mappers.
     *
     * @param doctorQuestionJpaRepository Repository for doctor question data access
     * @param medicalConcernJpaRepository Repository for medical concern data access
     * @param doctorQuestionsMapper       Mapper for doctor question domain objects and entities
     * @param medicalConcernMapper        Mapper for medical concern domain objects and entities
     */
    public MedicalConcernRepositoryImpl(DoctorQuestionJpaRepository doctorQuestionJpaRepository, MedicalConcernJpaRepository medicalConcernJpaRepository, DoctorQuestionsMapper doctorQuestionsMapper, MedicalConcernMapper medicalConcernMapper) {
        this.doctorQuestionJpaRepository = doctorQuestionJpaRepository;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
        this.doctorQuestionsMapper = doctorQuestionsMapper;
        this.medicalConcernMapper = medicalConcernMapper;
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
        List<DoctorQuestionEntity> entities = this.doctorQuestionJpaRepository.findAllByMedicalConcern_Id(medicalConcern.getId());
        return entities.stream().map(doctorQuestionsMapper::toDomain).toList();
    }

    @Override
    public Question getQuestionById(UUID uuid) throws QuestionNotFoundException {
        DoctorQuestionEntity entity = this.doctorQuestionJpaRepository.findById(uuid).orElseThrow(QuestionNotFoundException::new);
        return this.doctorQuestionsMapper.toDomain(entity);
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
}
