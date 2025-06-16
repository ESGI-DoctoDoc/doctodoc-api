package fr.esgi.doctodocapi.use_cases.user.manage_account;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.UserAlreadyExistException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user registration.
 * <p>
 * This service handles creation of a new user account, ensures uniqueness of email/phone,
 * and sends an account validation email upon successful registration.
 * </p>
 */
@Service
public class RegisterUser {

    private final UserRepository userRepository;
    private final SendAccountValidationEmail sendAccountValidationEmail;

    /**
     * Constructs the registration service with required dependencies.
     *
     * @param userRepository             the repository for user persistence
     * @param sendAccountValidationEmail service for sending account validation emails
     */
    public RegisterUser(UserRepository userRepository, SendAccountValidationEmail sendAccountValidationEmail) {
        this.userRepository = userRepository;
        this.sendAccountValidationEmail = sendAccountValidationEmail;
    }

    /**
     * Registers a new user account with the provided data.
     * <p>
     * If the email or phone number is already used, the operation fails.
     * On success, the user is persisted and a verification email is sent.
     * </p>
     *
     * @param registerRequest the data needed to create a new user
     * @return a {@link RegisterResponse} object indicating success
     * @throws UserAlreadyExistException if a user with the same email or phone already exists
     * @throws ApiException              if domain constraints are violated during creation
     */
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
