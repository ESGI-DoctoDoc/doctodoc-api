package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;

import java.util.List;
import java.util.UUID;

public interface MedicalConcernRepository {
    List<MedicalConcern> getMedicalConcerns(Doctor doctor);
    List<Question> getDoctorQuestions(MedicalConcern medicalConcern);

    Question getQuestionById(UUID uuid) throws QuestionNotFoundException;
    MedicalConcern getById(UUID id) throws MedicalConcernNotFoundException;
    MedicalConcern save(MedicalConcern medicalConcern);
}
