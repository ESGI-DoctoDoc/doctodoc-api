package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.exceptions.AuthenticationException;
import fr.esgi.doctodocapi.exceptions.AuthentificationMessageException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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


    public LoginResponse login(LoginRequest loginRequest, String host) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password().trim();

        try {
            this.authenticate(identifier, password);

            User userFoundByIdentifier = this.userRepository.findByEmailOrPhoneNumber(identifier, identifier);
            UUID userId = userFoundByIdentifier.getId();
            String email = userFoundByIdentifier.getEmail();

            this.verifyEmail(userFoundByIdentifier, host);

            boolean isPatientExist = this.patientRepository.isExistByUserId(userId);
            if (isPatientExist) {

                this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);
                String token = this.generatorToken.generate(email, "PATIENT", 2);
                return new LoginResponse(token);

            } else {
                throw new AuthenticationException(AuthentificationMessageException.BAD_CREDENTIALS);
            }

        } catch (Exception e) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_CREDENTIALS);
        }
    }

    public LoginResponse validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        try {

            User userFoundByEmail = this.userRepository.findByEmail(email);

            if (userFoundByEmail.getDoubleAuthCode().equals(doubleAuthCode)) {
                this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());

                String token = this.generatorToken.generate(email, "PATIENT", 120);
                return new LoginResponse(token);
            } else {
                throw new AuthenticationException(AuthentificationMessageException.BAD_DOUBLE_AUTH_CODE);
            }

        } catch (Exception e) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_DOUBLE_AUTH_CODE);
        }

    }

    private void authenticate(String username, String password) {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_CREDENTIALS);
        }
    }

    private void verifyEmail(User user, String host) {
        if (!user.isEmailVerified()) {
            this.sendEmailToValidateAccount(user.getEmail(), user.getId(), host);
            throw new AuthenticationException(AuthentificationMessageException.ACCOUNT_NOT_ACTIVATED);
        }
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
        this.messageSender.sendMessage(user.getPhoneNumber(), text);
    }

}
