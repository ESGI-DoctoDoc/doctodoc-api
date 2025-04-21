package fr.esgi.doctodocapi.infrastructure.brevo;

import fr.esgi.doctodocapi.model.user.MailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderImpl implements MailSender {
    @Override
    public void sendMail(String to, String subject, String body) {
        System.out.println("send mail");
    }
}
