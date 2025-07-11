package fr.esgi.doctodocapi.configuration.user;

import fr.esgi.doctodocapi.model.admin.AdminRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.use_cases.user.authentication.AuthenticateDoctor;
import fr.esgi.doctodocapi.use_cases.user.authentication.AuthenticatePatient;
import fr.esgi.doctodocapi.use_cases.user.authentication.AuthenticateUser;
import fr.esgi.doctodocapi.use_cases.user.manage_account.ChangePassword;
import fr.esgi.doctodocapi.use_cases.user.manage_account.RegisterUser;
import fr.esgi.doctodocapi.use_cases.user.manage_account.ResetPassword;
import fr.esgi.doctodocapi.use_cases.user.ports.in.*;
import fr.esgi.doctodocapi.use_cases.user.ports.out.AuthenticateUserInContext;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageUserAccountAuthentication {

    @Bean
    public IAuthenticatePatient authenticatePatient(IAuthenticateUser authenticateUser, TokenManager tokenManager, PatientRepository patientRepository) {
        return new AuthenticatePatient(authenticateUser, patientRepository, tokenManager);
    }

    @Bean
    public IAuthenticateDoctor authenticateDoctor(IAuthenticateUser authenticateUser, TokenManager tokenManager, DoctorRepository doctorRepository, AdminRepository adminRepository) {
        return new AuthenticateDoctor(authenticateUser, tokenManager, doctorRepository, adminRepository);
    }

    @Bean
    public IAuthenticateUser authenticateUser(AuthenticateUserInContext authenticateUserInContext, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, MessageSender messageSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator, TokenManager tokenManager, ISendAccountValidationEmail sendAccountValidationEmail, AdminRepository adminRepository) {
        return new AuthenticateUser(authenticateUserInContext, getCurrentUserContext, userRepository, messageSender, doubleAuthCodeGenerator, tokenManager, sendAccountValidationEmail, adminRepository);
    }

    @Bean
    public IRegisterUser registerUser(UserRepository userRepository, ISendAccountValidationEmail sendAccountValidationEmail) {
        return new RegisterUser(userRepository, sendAccountValidationEmail);
    }

    @Bean
    public IResetPassword resetPassword(UserRepository userRepository, TokenManager tokenManager, MailSender mailSender) {
        return new ResetPassword(userRepository, tokenManager, mailSender);
    }

    @Bean
    public IChangePassword changePassword(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository) {
        return new ChangePassword(getCurrentUserContext, userRepository);
    }
}
