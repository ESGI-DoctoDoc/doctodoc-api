package fr.esgi.doctodocapi.infrastructure.brevo;

import fr.esgi.doctodocapi.model.user.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of the MailSender interface using Spring's JavaMailSender.
 * <p>
 * Sends simple plain-text emails and logs the success or failure of the sending process.
 */
@Service
public class MailSenderImpl implements MailSender {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderImpl.class);

    private static final String FROM = "doctodoc.cestmieuxquedoctolib@gmail.com";

    private final JavaMailSender mailSender;

    /**
     * Constructs a MailSenderImpl with the specified JavaMailSender.
     *
     * @param mailSender the JavaMailSender instance used to send emails
     */
    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a simple plain-text email to the specified recipient.
     * Logs the outcome of the email sending operation.
     *
     * @param to      the recipient email address
     * @param subject the subject of the email
     * @param body    the plain-text body content of the email
     */
    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Mail sent successfully: Subject = {}, To = {}, From = {}", message.getSubject(), message.getTo(), message.getFrom());
        } catch (MailException ex) {
            logger.error("Failed to send mail: Subject = {}, To = {}, From = {}", message.getSubject(), message.getTo(), message.getFrom(), ex);
        }
    }
}
