package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.model.user.DoubleAuthCodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class DoubleAuthCodeGeneratorImpl implements DoubleAuthCodeGenerator {
    @Override
    public String generateDoubleAuthCode() {
        return "AABBCC";
    }
}
