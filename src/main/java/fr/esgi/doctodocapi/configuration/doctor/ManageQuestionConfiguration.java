package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question.DeleteObsoleteQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question.GetAllQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question.SaveQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question.SynchronizeQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IDeleteObsoleteQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IGetAllQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.ISaveQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.ISynchronizeQuestions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageQuestionConfiguration {
    @Bean
    public IGetAllQuestions getAllQuestions(MedicalConcernRepository medicalConcernRepository) {
        return new GetAllQuestions(medicalConcernRepository);
    }

    @Bean
    public IDeleteObsoleteQuestions deleteObsoleteQuestions(QuestionRepository questionRepository) {
        return new DeleteObsoleteQuestions(questionRepository);
    }

    @Bean
    public ISaveQuestions saveQuestions(QuestionRepository questionRepository) {
        return new SaveQuestions(questionRepository);
    }

    @Bean
    public ISynchronizeQuestions synchronizeQuestions(MedicalConcernRepository medicalConcernRepository, IDeleteObsoleteQuestions deleteObsoleteQuestions, ISaveQuestions saveQuestions) {
        return new SynchronizeQuestions(medicalConcernRepository, deleteObsoleteQuestions, saveQuestions);
    }
}
