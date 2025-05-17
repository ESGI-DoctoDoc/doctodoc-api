package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;

import java.util.List;
import java.util.UUID;

public interface MedicalConcernRepository {
    List<MedicalConcern> getMedicalConcerns(UUID doctorId);

    List<Question> getDoctorQuestions(UUID doctorId);

    MedicalConcern getMedicalConcernById(UUID medicalConcernId) throws MedicalConcernNotFoundException;
}
