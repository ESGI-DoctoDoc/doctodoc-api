package fr.esgi.doctodocapi.model.user;

import fr.esgi.doctodocapi.infrastructure.brevo.Invitation;

/**
 * Interface for sending emails.
 */
public interface MailSender {

    /**
     * Sends an email.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param body    the content of the email
     */
    void sendMail(String to, String subject, String body);

    void sendMail(String to, String subject, String body, Invitation invitation);
}
