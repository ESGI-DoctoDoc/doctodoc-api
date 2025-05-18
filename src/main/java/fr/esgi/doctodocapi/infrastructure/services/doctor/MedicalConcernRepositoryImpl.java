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
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicalConcernRepositoryImpl implements MedicalConcernRepository {
    private final DoctorQuestionJpaRepository doctorQuestionJpaRepository;
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    private final DoctorQuestionsMapper doctorQuestionsMapper;
    private final MedicalConcernMapper medicalConcernMapper;

    public MedicalConcernRepositoryImpl(DoctorQuestionJpaRepository doctorQuestionJpaRepository, MedicalConcernJpaRepository medicalConcernJpaRepository, DoctorQuestionsMapper doctorQuestionsMapper, MedicalConcernMapper medicalConcernMapper) {
        this.doctorQuestionJpaRepository = doctorQuestionJpaRepository;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
        this.doctorQuestionsMapper = doctorQuestionsMapper;
        this.medicalConcernMapper = medicalConcernMapper;
    }


    @Override
    public List<MedicalConcern> getMedicalConcerns(Doctor doctor) {
        List<MedicalConcernEntity> entities = this.medicalConcernJpaRepository.findAllByDoctor_Id(doctor.getId());
        return entities.stream().map(medicalConcernMapper::toDomain).toList();
    }

    @Override
    public List<Question> getDoctorQuestions(MedicalConcern medicalConcern) {
        List<DoctorQuestionEntity> entities = this.doctorQuestionJpaRepository.findAllByMedicalConcern_Id(medicalConcern.getId());
        return entities.stream().map(doctorQuestionsMapper::toDomain).toList();
    }

    @Override
    public MedicalConcern getById(UUID medicalConcernId) throws MedicalConcernNotFoundException {
        MedicalConcernEntity medicalConcern = this.medicalConcernJpaRepository.findById(medicalConcernId).orElseThrow(MedicalConcernNotFoundException::new);
        return this.medicalConcernMapper.toDomain(medicalConcern);
    }
}
