package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.use_cases.user.ports.in.AuthenticateUserInContext;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static fr.esgi.doctodocapi.exceptions.authentication.AuthentificationMessageException.*;

@Service
public class AuthenticateUser {
    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;
    private static final int TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES = 2;

    private final AuthenticateUserInContext authenticateUserInContext;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MessageSender messageSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;
    private final GeneratorToken generatorToken;
    private final SendAccountValidationEmail sendAccountValidationEmail;

    public AuthenticateUser(AuthenticateUserInContext authenticateUserInContext, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, MessageSender messageSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator, GeneratorToken generatorToken, SendAccountValidationEmail sendAccountValidationEmail) {
        this.authenticateUserInContext = authenticateUserInContext;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.messageSender = messageSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
        this.generatorToken = generatorToken;
        this.sendAccountValidationEmail = sendAccountValidationEmail;
    }

    public LoginResponse loginUser(LoginRequest loginRequest, String role) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password();

        this.authenticateUserInContext.persistAuthentication(identifier, password);

        String userRole = this.getCurrentUserContext.getRole();

        User userFoundByIdentifier = this.getUserByEmailOrPhoneNumber(identifier, identifier);

        if (userRole.equals(UserRoles.ADMIN.name())) {
            String token = this.generatorToken.generate(userFoundByIdentifier.getEmail().getValue(), role,
                    TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);
            return new LoginResponse(token);
        }

        this.verifyEmail(userFoundByIdentifier);
        this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);

        String token = this.generatorToken.generate(userFoundByIdentifier.getEmail().getValue(), role,
                TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES);
        return new LoginResponse(token);
    }

    public DoubleAuthenticationResponse validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();

        String email = this.getCurrentUserContext.getUsername();
        String role = this.getCurrentUserContext.getRole();

        try {

            User userFoundByEmail = this.userRepository.findByEmail(email);

            if (userFoundByEmail.getDoubleAuthCode().equals(doubleAuthCode)) {
                this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());

                boolean userHasOnBoardingDone = this.userHasOnBoardingDone(userFoundByEmail.getId());

                String token = this.generatorToken.generate(email, role, TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);
                return new DoubleAuthenticationResponse(token, userHasOnBoardingDone);
            } else {
                throw new AuthenticationException(BAD_DOUBLE_AUTH_CODE);
            }

        } catch (UserNotFoundException e) {
            throw new AuthenticationException(BAD_DOUBLE_AUTH_CODE);
        }

    }

    private User getUserByEmailOrPhoneNumber(String email, String phoneNumber) {
        try {
            return this.userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(BAD_CREDENTIALS);
        }

    }

    private boolean userHasOnBoardingDone(UUID userId) {
        String currentRole = this.getCurrentUserContext.getRole();

        if (currentRole.equals(UserRoles.PATIENT.name())) {
            return this.patientRepository.isExistByUserId(userId);
        }

        if (currentRole.equals(UserRoles.DOCTOR.name())) {
            return this.doctorRepository.isExistByUserId(userId);
        }

        return false;
    }

    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user.getId());

        String text = "Voici le code de vérification lié à votre compte Doctodoc : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber().getValue(), text);
    }

    private void verifyEmail(User userFoundByIdentifier) {
        if (!userFoundByIdentifier.isEmailVerified()) {
            this.sendAccountValidationEmail.send(userFoundByIdentifier.getEmail().getValue(), userFoundByIdentifier.getId());
            throw new AuthenticationException(ACCOUNT_NOT_ACTIVATED);
        }
    }
}
