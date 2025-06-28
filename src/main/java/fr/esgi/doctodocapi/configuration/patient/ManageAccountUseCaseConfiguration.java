package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.manage_account.SendAccountValidationEmail;
import fr.esgi.doctodocapi.use_cases.user.manage_account.ValidateEmail;
import fr.esgi.doctodocapi.use_cases.user.ports.in.ISendAccountValidationEmail;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IValidateEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageAccountUseCaseConfiguration {
    @Bean
    public IValidateEmail validateEmail(UserRepository userRepository) {
        return new ValidateEmail(userRepository);
    }

    @Bean
    public ISendAccountValidationEmail sendAccountValidationEmail(MailSender mailSender) {
        return new SendAccountValidationEmail(mailSender);
    }
}
