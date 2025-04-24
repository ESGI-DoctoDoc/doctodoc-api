package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.exceptions.AuthenticationException;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AdminJpaRepository;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentRequestContext;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static fr.esgi.doctodocapi.exceptions.AuthentificationMessageException.*;

@Service
public class AuthenticateUser {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticateUser.class);

    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;
    private static final int TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES = 2;

    private final GetCurrentUserContext getCurrentUserContext;
    private final GetCurrentRequestContext getCurrentRequestContext;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AdminJpaRepository adminJpaRepository;
    private final MessageSender messageSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;
    private final GeneratorToken generatorToken;
    private final MailSender mailSender;


    public AuthenticateUser(GetCurrentUserContext getCurrentUserContext, GetCurrentRequestContext getCurrentRequestContext, AuthenticationManager authenticationManager, UserRepository userRepository, AdminJpaRepository adminJpaRepository, MessageSender messageSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator, GeneratorToken generatorToken, MailSender mailSender) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.getCurrentRequestContext = getCurrentRequestContext;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.adminJpaRepository = adminJpaRepository;
        this.messageSender = messageSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
        this.generatorToken = generatorToken;
        this.mailSender = mailSender;
    }

    public LoginResponse loginUser(LoginRequest loginRequest, String role) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password();

        this.authenticate(identifier, password);

        User userFoundByIdentifier;

        try {
            userFoundByIdentifier = this.userRepository.findByEmailOrPhoneNumber(identifier, identifier);
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(BAD_CREDENTIALS);
        }

        if (this.adminJpaRepository.existsByUser_id(userFoundByIdentifier.getId())) {
            String token = this.generatorToken.generate(userFoundByIdentifier.getEmail(), role,
                    TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);
            return new LoginResponse(token);
        }

        this.verifyEmail(userFoundByIdentifier);
        this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);

        String token = this.generatorToken.generate(userFoundByIdentifier.getEmail(), role, TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES);
        return new LoginResponse(token);
    }

    public LoginResponse validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();
        String role = "";

        String email = this.getCurrentUserContext.getUsername();
        String[] roles = this.getCurrentUserContext.getRole();

        if (roles.length == 1) {
            role = roles[0];
        }

        try {
            User userFoundByEmail = this.userRepository.findByEmail(email);

            if (userFoundByEmail.getDoubleAuthCode().equals(doubleAuthCode)) {
                this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());

                String token = this.generatorToken.generate(email, role, TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);
                return new LoginResponse(token);
            } else {
                throw new AuthenticationException(BAD_DOUBLE_AUTH_CODE);
            }

        } catch (Exception e) {
            throw new AuthenticationException(BAD_DOUBLE_AUTH_CODE);
        }

    }

    private void authenticate(String username, String password) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new AuthenticationException(BAD_CREDENTIALS);
        }
    }

    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user.getId());

        String text = "Voici le code de vérification lié à votre compte Doctodoc : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber(), text);
    }

    private void verifyEmail(User userFoundByIdentifier) {
        if (!userFoundByIdentifier.isEmailVerified()) {
            this.sendEmailToValidateAccount(userFoundByIdentifier.getEmail(), userFoundByIdentifier.getId());
            throw new AuthenticationException(ACCOUNT_NOT_ACTIVATED);
        }
    }

    private void sendEmailToValidateAccount(String email, UUID userId) {
        String currentDomainName = this.getCurrentRequestContext.getCurrentDomain();

        if (!currentDomainName.isEmpty()) {
            String subject = "Activer votre compte sur Doctodoc";
            String url = currentDomainName + "/api/v1/users/verify-email/" + userId.toString();
            String body = String.format("""
                    Merci de cliquer sur le lien pour valider votre compte Doctodoc
                    %s
                    Ce mail est valide 30 minutes.
                    L'équipe de Doctodoc""", url);

            this.mailSender.sendMail(email, subject, body);

        } else {
            logger.error("Failed to create url");
        }

    }

}
