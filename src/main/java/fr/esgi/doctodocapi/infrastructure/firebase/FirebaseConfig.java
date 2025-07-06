package fr.esgi.doctodocapi.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    FirebaseApp firebaseApp() {
        try {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream(firebaseConfigPath);

            if (serviceAccount == null) {
                throw new IllegalStateException("Fichier firebase-service-account-key.json introuvable dans les ressources.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            logger.error("Erreur lors de l'initialisation de Firebase", e);
            throw new IllegalStateException("Impossible d'initialiser Firebase", e);
        }
    }
}
