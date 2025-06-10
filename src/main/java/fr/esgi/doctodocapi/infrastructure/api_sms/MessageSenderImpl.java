package fr.esgi.doctodocapi.infrastructure.api_sms;

import fr.esgi.doctodocapi.domain.entities.user.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public class MessageSenderImpl implements MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);

    @Value("${top.message.url}")
    private String apiUrl;

    @Value("${top.message.apiKey}")
    private String apiKey;


    @Override
    public void sendMessage(String phoneNumber, String message) {

        String json = String.format("""
                {
                    "data": {
                        "from": "TopMessage",
                        "to": [
                            "%s"
                        ],
                        "text": "%s"
                    }
                }
                """, phoneNumber, message);
        HttpRequest.BodyPublishers.ofString(json);

//        try {
//            String response = HttpClient.newHttpClient().send(
//                    HttpRequest.newBuilder()
//                            .uri(URI.create(apiUrl))
//                            .header("Content-Type", "application/json")
//                            .header("X-TopMessage-Key", apiKey)
//                            .POST(HttpRequest.BodyPublishers.ofString(json))
//                            .build(),
//                    HttpResponse.BodyHandlers.ofString()
//            ).body();
//            logger.info("Message sent : {}", response);
        logger.info("Message sent : {}", message);

//        } catch (IOException | InterruptedException e) {
//            logger.error("Error in sending message");
//            throw new MessageFailed();
//        }


    }
}
