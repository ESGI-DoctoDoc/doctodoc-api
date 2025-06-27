package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.GetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.OnboardingDoctorProcess;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information.IGetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IOnboardingDoctor;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorAccountConfiguration {

    @Bean
    public IOnboardingDoctor onboardingDoctorProcess(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        return new OnboardingDoctorProcess(doctorRepository, userRepository, getCurrentUserContext);
    }

    @Bean
    public IGetDoctorInformation getDoctorInformation(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository) {
        return new GetDoctorInformation(userRepository, doctorRepository, getCurrentUserContext, doctorSubscriptionRepository);
    }
}
