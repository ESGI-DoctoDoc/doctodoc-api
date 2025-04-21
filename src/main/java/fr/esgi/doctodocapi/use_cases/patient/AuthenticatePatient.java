package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requets.LoginViaEmailRequest;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatePatient {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final MessageSender messageSender;
    private final MailSender mailSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;


    public AuthenticatePatient(AuthenticationManager authenticationManager, UserRepository userRepository, PatientRepository patientRepository, MessageSender messageSender, MailSender mailSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.messageSender = messageSender;
        this.mailSender = mailSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
    }

    // todo : voir le return
    public String loginWithEmail(LoginViaEmailRequest loginViaEmailRequest) {
        String email = loginViaEmailRequest.email().trim();
        String password = loginViaEmailRequest.password().trim();
        this.authenticate(email, password);

        User userFoundByEmail = this.userRepository.findByEmail(email);

        if (!userFoundByEmail.isEmailVerified()) {
            this.sendEmailToValidateAccount(email);
            return "send email to activate your account";
        }

        boolean isPatientExist = this.patientRepository.isExistByUserId(userFoundByEmail.getId());
        if (isPatientExist) {
            this.sendMessageWithDoubleAuthCode(userFoundByEmail);
            return "send message to validate double auth code";
        } else {
            throw new AuthenticationException();
        }

    }


    private void authenticate(String username, String password) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private void sendEmailToValidateAccount(String email) {
        String subject = "Activer votre compte sur Doctodoc";

        String body = """
                Merci de cliquer sur le lien pour valider votre compte Doctodoc
                lien...
                L'équipe de Doctodoc""";

        this.mailSender.sendMail(email, subject, body);
    }

    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user);

        String text = "Voici le code de verification : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber(), text);
    }

}
