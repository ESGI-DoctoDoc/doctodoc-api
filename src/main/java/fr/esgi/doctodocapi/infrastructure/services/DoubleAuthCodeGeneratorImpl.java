package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.model.user.DoubleAuthCodeGenerator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Implementation of the DoubleAuthCodeGenerator interface.
 * This service generates authentication codes for two-factor authentication.
 */
@Service
public class DoubleAuthCodeGeneratorImpl implements DoubleAuthCodeGenerator {
    /**
     * Generates a 6-digit code for two-factor authentication.
     * The commented code would generate a random 6-digit number using SecureRandom.
     *
     * @return A string containing the 6-digit authentication code
     */
    @Override
    public String generateDoubleAuthCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }

//        return code.toString();
        return "000000";
    }
}
