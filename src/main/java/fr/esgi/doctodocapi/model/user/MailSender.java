package fr.esgi.doctodocapi.model.user;

public interface MailSender {
    void sendMail(String to, String subject, String body);
}
