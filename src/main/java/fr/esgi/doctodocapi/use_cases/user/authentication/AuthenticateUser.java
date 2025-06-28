package fr.esgi.doctodocapi.use_cases.user.authentication;

import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.use_cases.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.use_cases.exceptions.authentication.AuthentificationMessageException;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IAuthenticateUser;
import fr.esgi.doctodocapi.use_cases.user.ports.in.ISendAccountValidationEmail;
import fr.esgi.doctodocapi.use_cases.user.ports.out.AuthenticateUserInContext;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;

import java.util.Objects;

/**
 * Service responsible for authenticating users into the system.
 * <p>
 * Handles password-based login, two-factor authentication (2FA) via SMS,
 * token generation, and email verification.
 * </p>
 */
public class AuthenticateUser implements IAuthenticateUser {

    private static final int TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES = 10;

    private final AuthenticateUserInContext authenticateUserInContext;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final MessageSender messageSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;
    private final TokenManager tokenManager;
    private final ISendAccountValidationEmail sendAccountValidationEmail;

    /**
     * Constructs the service with its required dependencies.
     *
     * @param authenticateUserInContext  component to persist authentication session
     * @param getCurrentUserContext      component to get current user's context
     * @param userRepository             user data access layer
     * @param messageSender              service to send SMS messages
     * @param doubleAuthCodeGenerator    service to generate double-authentication codes
     * @param tokenManager               utility to generate JWT tokens
     * @param sendAccountValidationEmail service to send email verification messages
     */
    public AuthenticateUser(AuthenticateUserInContext authenticateUserInContext,
                            GetCurrentUserContext getCurrentUserContext,
                            UserRepository userRepository,
                            MessageSender messageSender,
                            DoubleAuthCodeGenerator doubleAuthCodeGenerator,
                            TokenManager tokenManager,
                            ISendAccountValidationEmail sendAccountValidationEmail) {
        this.authenticateUserInContext = authenticateUserInContext;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.messageSender = messageSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
        this.tokenManager = tokenManager;
        this.sendAccountValidationEmail = sendAccountValidationEmail;
    }

    /**
     * Authenticates the user using the provided credentials and returns a short-lived or long-lived token.
     * <p>
     * If the user is an admin, a long-lived token is immediately returned.
     * For other users, a 2FA code is sent via SMS and a short-lived token is returned.
     * </p>
     *
     * @param loginRequest the login form containing the user identifier and password
     * @return a {@link LoginResponse} containing a JWT token
     * @throws AuthenticationException if credentials are invalid or account is not verified
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password();

        this.authenticateUserInContext.persistAuthentication(identifier, password);

        String userRole = this.getCurrentUserContext.getRole();
        User userFoundByIdentifier = this.getUserByEmailOrPhoneNumber(identifier, identifier);

        this.verifyEmail(userFoundByIdentifier);
        this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);

        String token = this.tokenManager.generate(
                userFoundByIdentifier.getEmail().getValue(),
                userRole,
                TOKEN_SHORT_TERM_EXPIRATION_IN_MINUTES
        );

        return new LoginResponse(token);
    }

    /**
     * Validates the double authentication code sent to the user.
     *
     * @param validateDoubleAuthRequest request containing the submitted 2FA code
     * @return the authenticated user if the code is correct
     * @throws AuthenticationException if the code is invalid or user not found
     */
    public User validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();
        String email = this.getCurrentUserContext.getUsername();

        try {
            User userFoundByEmail = this.userRepository.findByEmail(email);

            if (Objects.equals(userFoundByEmail.getDoubleAuthCode(), doubleAuthCode)) {
                this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());
                return userFoundByEmail;
            } else {
                throw new AuthenticationException(AuthentificationMessageException.BAD_DOUBLE_AUTH_CODE);
            }

        } catch (UserNotFoundException e) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_DOUBLE_AUTH_CODE);
        }
    }

    /**
     * Attempts to find a user by either email or phone number.
     *
     * @param email       the email input
     * @param phoneNumber the phone number input
     * @return the user if found
     * @throws AuthenticationException if no matching user is found
     */
    private User getUserByEmailOrPhoneNumber(String email, String phoneNumber) {
        try {
            return this.userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_CREDENTIALS);
        }
    }

    /**
     * Sends a double authentication code by SMS to the given user.
     * The code is stored in the database temporarily.
     *
     * @param user the user to send the code to
     */
    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user.getId());

        String text = "Voici le code de vérification lié à votre compte Doctodoc : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber().getValue(), text);
    }

    /**
     * Checks if the user's email is verified.
     * If not, a verification email is sent and an exception is thrown.
     *
     * @param userFoundByIdentifier the user to verify
     * @throws AuthenticationException if the email is not verified
     */
    private void verifyEmail(User userFoundByIdentifier) {
        if (!userFoundByIdentifier.isEmailVerified()) {
            this.sendAccountValidationEmail.send(
                    userFoundByIdentifier.getEmail().getValue(),
                    userFoundByIdentifier.getId()
            );
            throw new AuthenticationException(AuthentificationMessageException.ACCOUNT_NOT_ACTIVATED);
        }
    }
}
