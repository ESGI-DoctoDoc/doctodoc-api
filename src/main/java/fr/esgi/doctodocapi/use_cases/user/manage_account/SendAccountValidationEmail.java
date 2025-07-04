package fr.esgi.doctodocapi.use_cases.user.manage_account;

import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.user.ports.in.ISendAccountValidationEmail;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Service responsible for sending account validation emails to newly registered users.
 * <p>
 * Builds a validation URL using the current request context and sends an email containing
 * the link to the user’s email address.
 * </p>
 */
public class SendAccountValidationEmail implements ISendAccountValidationEmail {

    private final MailSender mailSender;

    /**
     * Constructs the email sender service with required dependencies.
     *
     * @param mailSender               abstraction for sending email messages
     */
    public SendAccountValidationEmail(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an email to the specified user with a link to validate their account.
     * <p>
     * The link is constructed using the domain name from the current request context.
     * If the domain cannot be determined, the method logs an error and does not send an email.
     * </p>
     *
     * @param email  the recipient's email address
     * @param userId the UUID of the user, used in the validation link
     */
    public void send(String email, UUID userId, String verificationUrl) {
        String url = verificationUrl + "?userId=" + userId;
        String subject = "Activer votre compte sur Doctodoc";
        String body = String.format("""
            Merci de cliquer sur le lien pour valider votre compte Doctodoc
            %s
            Ce mail est valide 30 minutes.
            L'équipe de Doctodoc""", url);

        this.mailSender.sendMail(email, subject, body);
    }
}