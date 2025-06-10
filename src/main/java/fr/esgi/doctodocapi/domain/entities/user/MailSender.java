package fr.esgi.doctodocapi.domain.entities.user;

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
}
