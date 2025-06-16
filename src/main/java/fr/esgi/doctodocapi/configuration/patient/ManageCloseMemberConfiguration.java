package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_close_member.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member.*;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageCloseMemberConfiguration {
    @Bean
    public ICreateCloseMember createCloseMember(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository) {
        return new CreateCloseMember(getCurrentUserContext, userRepository, patientRepository);
    }

    @Bean
    public IDeleteCloseMember deleteCloseMember(PatientRepository patientRepository) {
        return new DeleteCloseMember(patientRepository);
    }

    @Bean
    public IGetAllCloseMembers getAllCloseMembers(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository) {
        return new GetAllCloseMembers(getCurrentUserContext, userRepository, patientRepository);
    }

    @Bean
    public IGetCloseMember getCloseMember(PatientRepository patientRepository) {
        return new GetCloseMember(patientRepository);
    }

    @Bean
    public IUpdateCloseMember updateCloseMember(PatientRepository patientRepository) {
        return new UpdateCloseMember(patientRepository);
    }
}
