package fr.esgi.doctodocapi.use_cases.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.*;
import fr.esgi.doctodocapi.error.exceptions.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthenticateDoctor {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MessageSender messageSender;
    private final DoubleAuthCodeGenerator doubleAuthCodeGenerator;
    private final GeneratorToken generatorToken;

    public AuthenticateDoctor(AuthenticationManager authenticationManager, UserRepository userRepository, DoctorRepository doctorRepository, MessageSender messageSender, DoubleAuthCodeGenerator doubleAuthCodeGenerator, GeneratorToken generatorToken) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.messageSender = messageSender;
        this.doubleAuthCodeGenerator = doubleAuthCodeGenerator;
        this.generatorToken = generatorToken;
    }

    public String login(LoginRequest loginRequest) {
        String identifier = loginRequest.identifier().trim();
        String password = loginRequest.password().trim();

        this.authenticate(identifier, password);

        User userFoundByIdentifier = this.userRepository.findByEmailOrPhoneNumber(identifier, identifier);
        UUID userId = userFoundByIdentifier.getId();
        String email = userFoundByIdentifier.getEmail().getValue();

        boolean isDoctorExist = this.doctorRepository.isExistByUserId(userId);
        if (isDoctorExist) {
            this.sendMessageWithDoubleAuthCode(userFoundByIdentifier);
            return this.generatorToken.generate(email, "DOCTOR", 2);
        } else {
            throw new AuthenticationException();
        }
    }

    public String validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        String doubleAuthCode = validateDoubleAuthRequest.doubleAuthCode().trim();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        User userFoundByEmail = this.userRepository.findByEmail(email);

        if(Objects.equals(userFoundByEmail.getDoubleAuthCode(), doubleAuthCode)) {
            this.userRepository.updateDoubleAuthCode(null, userFoundByEmail.getId());
            return this.generatorToken.generate(email, "DOCTOR", 2);
        } else {
            throw new AuthenticationException();
        }
    }

    private void authenticate(String username, String password) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private void sendMessageWithDoubleAuthCode(User user) {
        String code = this.doubleAuthCodeGenerator.generateDoubleAuthCode();
        this.userRepository.updateDoubleAuthCode(code, user.getId());

        String text = "Voici le code de vérification pour valider le numéro de téléphone lié à votre compte Doctodoc : " + code;
        this.messageSender.sendMessage(user.getPhoneNumber().getValue(), text);
    }
}
