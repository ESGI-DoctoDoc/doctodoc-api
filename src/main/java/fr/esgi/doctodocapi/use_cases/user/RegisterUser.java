package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.exceptions.UserAlreadyExistException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class RegisterUser {
    private final UserRepository userRepository;
    private final SendAccountValidationEmail sendAccountValidationEmail;

    public RegisterUser(UserRepository userRepository, SendAccountValidationEmail sendAccountValidationEmail) {
        this.userRepository = userRepository;
        this.sendAccountValidationEmail = sendAccountValidationEmail;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.email().toLowerCase().trim();
        String password = registerRequest.password().trim();
        String phoneNumber = registerRequest.phoneNumber().trim();

        boolean isExistUser = this.userRepository.isExistUser(email, phoneNumber);
        if (isExistUser) {
            throw new UserAlreadyExistException();
        }

        try {
            User user = User.create(email, password, phoneNumber);
            User userSaved = this.userRepository.save(user);
            this.sendAccountValidationEmail.send(userSaved.getEmail().getValue(), userSaved.getId());
            return new RegisterResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
