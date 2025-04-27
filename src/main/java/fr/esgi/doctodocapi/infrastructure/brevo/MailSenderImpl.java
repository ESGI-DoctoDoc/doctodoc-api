package fr.esgi.doctodocapi.infrastructure.brevo;

import fr.esgi.doctodocapi.model.user.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderImpl implements MailSender {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderImpl.class);

    private static final String FROM = "doctodoc.cestmieuxquedoctolib@gmail.com";

    private final JavaMailSender mailSender;

    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Mail send: Subject = {}, To = {}, From = {}", message.getSubject(), message.getTo(), message.getFrom());
        } catch (MailException ex) {
            logger.error("Error Mail : Subject = {}, To = {}, From = {}", message.getSubject(), message.getTo(), message.getFrom());

        }
    }
}
