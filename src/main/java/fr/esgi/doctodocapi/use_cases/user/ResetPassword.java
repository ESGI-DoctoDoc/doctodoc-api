package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.ResetPasswordRequest;
import fr.esgi.doctodocapi.dtos.requests.UpdatePasswordRequest;
import fr.esgi.doctodocapi.dtos.responses.RequestResetPasswordResponse;
import fr.esgi.doctodocapi.dtos.responses.UpdatePasswordResponse;
import fr.esgi.doctodocapi.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.model.vo.password.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static fr.esgi.doctodocapi.exceptions.authentication.AuthentificationMessageException.*;

/**
 * Service responsible for handling password reset use cases.
 * <p>
 * This service provides two main operations:
 * <ul>
 *   <li>requestResetPassword: generate a reset-token for a user and send a reset link by email</li>
 *   <li>updatePassword: validate the reset-token and update the user's password</li>
 * </ul>
 * Errors such as user not found or invalid token will result in an AuthenticationException.
 * </p>
 */
@Service
public class ResetPassword {
    private static final int RESET_TOKEN_EXPIRATION_MINUTES = 15;

    /**
     * Domain or base URL of the front-end application. Injected from application properties (e.g., ${app.front.url}).
     */
    @Value("${app.front.url}")
    private String domain;

    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final MailSender mailSender;

    /**
     * Constructs the ResetPassword service with the required dependencies.
     *
     * @param userRepository  repository for retrieving and updating User entities
     * @param tokenManager  utility to generate and parse JWT or similar tokens
     * @param mailSender      service responsible for sending emails
     */
    public ResetPassword(UserRepository userRepository, TokenManager tokenManager, MailSender mailSender) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
        this.mailSender = mailSender;
    }

    /**
     * Initiates the password reset process for a given email address.
     * <p>
     * Steps:
     * <ol>
     *   <li>Trim and validate the email from the request.</li>
     *   <li>Generate a reset token valid for {@link #RESET_TOKEN_EXPIRATION_MINUTES} minutes, with role "RESET_PASSWORD".</li>
     *   <li>Compose a reset URL: <code>{domain}auth/resetPassword?token={token}</code>.</li>
     *   <li>Email the user with the reset URL and instructions.</li>
     *   <li>Return an empty {@link RequestResetPasswordResponse} on success.</li>
     * </ol>
     *
     * @param request the DTO containing the email to send the reset link to
     * @return a {@link RequestResetPasswordResponse} indicating that the request was processed
     * @throws AuthenticationException if any unexpected error occurs during the process
     */
    public RequestResetPasswordResponse requestResetPassword(ResetPasswordRequest request) {
        try {
            String email = request.email().trim();
            User user = this.userRepository.findByEmail(email);

            String token = this.tokenManager.generate(user.getEmail().getValue(), "RESET_PASSWORD", RESET_TOKEN_EXPIRATION_MINUTES);
            String subject = "Réinitialisation de votre mot de passe Doctodoc";
            String url = domain + "auth/resetPassword?token=" + token;
            String body = String.format("""
                    Bonjour,
                    
                    Vous avez demandé à réinitialiser votre mot de passe sur Doctodoc.
                    
                    Merci de cliquer sur le lien suivant (valide %d minutes) :
                    %s
                    
                    Si vous n'êtes pas à l'origine de cette demande, ignorez simplement ce mail.
                    
                    L'équipe de Doctodoc
                    """, RESET_TOKEN_EXPIRATION_MINUTES, url);

            this.mailSender.sendMail(email, subject, body);
            return new RequestResetPasswordResponse();
        } catch (Exception e) {
            throw new AuthenticationException(RESET_PASSWORD_ERROR);
        }
    }

    /**
     * Validates the reset token and updates the user's password to the new one provided.
     * <p>
     * Steps:
     * <ol>
     *   <li>Extract the email (username) from the token via {@link TokenManager#extractUserName(String)}</li>
     *   <li>Extract the role from the token via {@link TokenManager#extractRole(String)}</li>
     *   <li>If the role is not "RESET_PASSWORD", throw an {@link AuthenticationException} with INVALID_TOKEN.</li>
     *   <li>Replace the user's password with a new {@link Password} value object encapsulating the provided new password.</li>
     *   <li>Persist the updated password via {@link UserRepository#updatePassword(User)}</li>
     *   <li>Return an empty {@link UpdatePasswordResponse} on success.</li>
     * </ol>
     *
     * @param request the DTO containing the reset token and the new password
     * @return an {@link UpdatePasswordResponse} indicating that the password was updated
     * @throws AuthenticationException if any unexpected error occurs during the process
     */
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        try {
            String email = this.tokenManager.extractUserName(request.token());
            String role = this.tokenManager.extractRole(request.token());

            if (!"RESET_PASSWORD".equals(role)) {
                throw new AuthenticationException(INVALID_TOKEN);
            }

            User user = userRepository.findByEmail(email);
            user.setPassword(Password.of(request.newPassword()));

            this.userRepository.updatePassword(user);
            return new UpdatePasswordResponse();
        } catch (Exception e) {
            throw new AuthenticationException(RESET_PASSWORD_ERROR);
        }
    }
}