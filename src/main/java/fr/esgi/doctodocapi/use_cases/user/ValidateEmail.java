package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.exceptions.CannotValidateEmail;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidateEmail {
    private final UserRepository userRepository;

    public ValidateEmail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UUID userId) {
        try {
            this.userRepository.validateEmail(userId);
        } catch (UserNotFoundException e) {
            throw new CannotValidateEmail();
        }
    }
}
