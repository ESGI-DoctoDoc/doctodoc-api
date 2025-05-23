package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.ResetPasswordRequest;
import fr.esgi.doctodocapi.dtos.requests.UpdatePasswordRequest;
import fr.esgi.doctodocapi.dtos.responses.RequestResetPasswordResponse;
import fr.esgi.doctodocapi.dtos.responses.UpdatePasswordResponse;
import fr.esgi.doctodocapi.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.model.vo.password.Password;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static fr.esgi.doctodocapi.exceptions.authentication.AuthentificationMessageException.*;

@Service
public class ResetPassword {
    private static final int RESET_TOKEN_EXPIRATION_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeneratorToken generatorToken;
    private final MailSender mailSender;

    public ResetPassword(UserRepository userRepository, PasswordEncoder passwordEncoder, GeneratorToken generatorToken, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.generatorToken = generatorToken;
        this.mailSender = mailSender;
    }

    public RequestResetPasswordResponse requestResetPassword(ResetPasswordRequest request) {
        try {
            String email = request.email().trim();
            User user = this.userRepository.findByEmail(email);

            String token = this.generatorToken.generate(user.getEmail().getValue(), "RESET_PASSWORD", RESET_TOKEN_EXPIRATION_MINUTES);

            String subject = "Réinitialisation de votre mot de passe Doctodoc";
            String url = "http://localhost:3000/auth/resetPassword?token=" + token;
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
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(EMAIL_NOT_FOUND);
        } catch (Exception e) {
            throw new AuthenticationException(RESET_PASSWORD_ERROR);
        }
    }

    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        try {
            String email = this.generatorToken.extractUserName(request.token());
            String role  = this.generatorToken.extractRole(request.token());

            if (!"RESET_PASSWORD".equals(role)) {
                throw new AuthenticationException(INVALID_TOKEN);
            }

            User user = userRepository.findByEmail(email);
            user.setPassword(Password.of(request.newPassword()));

            this.userRepository.updatePassword(user);
            return new UpdatePasswordResponse();
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(EMAIL_NOT_FOUND);
        } catch (Exception e) {
            throw new AuthenticationException(RESET_PASSWORD_ERROR);
        }
    }
}