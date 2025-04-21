package fr.esgi.doctodocapi.infrastructure.api_sms;

import fr.esgi.doctodocapi.model.user.MessageSender;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderImpl implements MessageSender {

    @Override
    public void sendMessage(String phoneNumber, String message) {
        System.out.println("send message");
    }
}
