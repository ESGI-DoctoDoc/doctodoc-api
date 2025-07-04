package fr.esgi.doctodocapi.infrastructure.message_config;
import fr.esgi.doctodocapi.use_cases.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);


    @Value("${api.key}")
    private String apiKey;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/");
        config.setApplicationDestinationPrefixes("/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                logger.info(">> STOMP interceptor triggered");

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    logger.info(">> STOMP CONNECT command detected");
                    String providedKey = accessor.getFirstNativeHeader("x-api-key");
                    logger.info(">> Provided x-api-key: {}", providedKey);

                    if (!apiKey.equals(providedKey)) {
                        logger.warn(">> Invalid API key provided: {}", providedKey);
                        throw new UnauthorizedException();
                    }

                    logger.info(">> API key validated successfully");
                }

                return message;
            }
        });
    }
}