package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentRequestContext;
import fr.esgi.doctodocapi.model.user.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SendAccountValidationEmail {

    private static final Logger logger = LoggerFactory.getLogger(SendAccountValidationEmail.class);

    private final GetCurrentRequestContext getCurrentRequestContext;
    private final MailSender mailSender;

    public SendAccountValidationEmail(GetCurrentRequestContext getCurrentRequestContext, MailSender mailSender) {
        this.getCurrentRequestContext = getCurrentRequestContext;
        this.mailSender = mailSender;
    }

    public void send(String email, UUID userId) {
        String currentDomainName = this.getCurrentRequestContext.getCurrentDomain();

        if (!currentDomainName.isEmpty()) {
            String subject = "Activer votre compte sur Doctodoc";
            String url = currentDomainName + "/api/v1/users/validate-email?userId=" + userId;
            String body = String.format("""
                    Merci de cliquer sur le lien pour valider votre compte Doctodoc
                    %s
                    Ce mail est valide 30 minutes.
                    L'équipe de Doctodoc""", url);

            this.mailSender.sendMail(email, subject, body);
        } else {
            logger.error("Failed to create URL for account activation email");
        }
    }
}