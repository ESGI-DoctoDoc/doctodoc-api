package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.error.exceptions.AuthenticationException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.model.user.email.Email;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthenticatePatient {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final MessageSender messageSender;
    private final MailSender mailSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;
    private final GeneratorToken generatorToken;


    public AuthenticatePatient(AuthenticationManager authenticationManager, UserRepository userRepository, PatientRepository patientRepository, MessageSender messageSender, MailSender mailSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator, GeneratorToken generatorToken) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.messageSender = messageSender;
        this.mailSender = mailSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
        this.generatorToken = generatorToken;
    }


    public String login(LoginRequest loginRequest, String host) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password().trim();

        this.authenticate(identifier, password);

        User userFoundByIdentifier = this.userRepository.findByEmailOrPhoneNumber(identifier, identifier);
        UUID userId = userFoundByIdentifier.getId();
        String email = userFoundByIdentifier.getEmail().getValue();


        if (!userFoundByIdentifier.isEmailVerified()) {
            this.sendEmailToValidateAccount(email, userId, host);
            return "send email to activate your account";
        }

        boolean isPatientExist = this.patientRepository.isExistByUserId(userId);
        if (isPatientExist) {
            this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);
            return this.generatorToken.generate(email, "PATIENT", 2);
        } else {
            throw new AuthenticationException();
        }
    }

    public String validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        User userFoundByEmail = this.userRepository.findByEmail(email);

        if (userFoundByEmail.isEmailVerified() && Objects.equals(userFoundByEmail.getDoubleAuthCode(), doubleAuthCode)) {
            this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());
            return this.generatorToken.generate(email, "PATIENT", 120);
        } else {
            throw new AuthenticationException();
        }

    }

    private void authenticate(String username, String password) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private void sendEmailToValidateAccount(String email, UUID userId, String host) {
        String subject = "Activer votre compte sur Doctodoc";
        String url = host + "api/v1/verify-email/" + userId.toString();
        String body = String.format("""
                Merci de cliquer sur le lien pour valider votre compte Doctodoc
                %s
                Ce mail est valide 30 minutes.
                L'équipe de Doctodoc""", url);

        this.mailSender.sendMail(email, subject, body);
    }

    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user.getId());

        String text = "Voici le code de vérification pour valider le numéro de téléphone lié à votre compte Doctodoc : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber().getValue(), text);
    }

}
