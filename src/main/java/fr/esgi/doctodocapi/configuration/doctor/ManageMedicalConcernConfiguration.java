package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.CreateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.GetAllMedicalConcerns;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.ICreateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.IGetAllMedicalConcerns;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IGetAllQuestions;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageMedicalConcernConfiguration {
    @Bean
    public ICreateMedicalConcern createMedicalConcern(MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository) {
        return new CreateMedicalConcern(medicalConcernRepository, userRepository, getCurrentUserContext, doctorRepository);
    }

    @Bean
    public IGetAllMedicalConcerns getAllMedicalConcerns(MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, IGetAllQuestions getAllQuestions) {
        return new GetAllMedicalConcerns(medicalConcernRepository, userRepository, doctorRepository, getCurrentUserContext, getAllQuestions);
    }
}
