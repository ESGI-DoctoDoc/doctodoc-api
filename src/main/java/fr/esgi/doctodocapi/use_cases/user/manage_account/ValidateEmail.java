package fr.esgi.doctodocapi.use_cases.user.manage_account;

import fr.esgi.doctodocapi.use_cases.exceptions.CannotValidateEmail;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateEmailRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.ValidateEmailResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IValidateEmail;

import java.util.UUID;

/**
 * Service responsible for validating a user's email.
 * <p>
 * Attempts to mark the email associated with the given user ID as validated.
 * Throws {@link CannotValidateEmail} if the user does not exist.
 * </p>
 */
public class ValidateEmail implements IValidateEmail {
    private final UserRepository userRepository;

    /**
     * Constructs the service with the required user repository.
     *
     * @param userRepository repository to perform user-related data operations
     */
    public ValidateEmail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the email of the user identified by the provided UUID.
     *
     * @param request with the UUID of the user whose email must be validated
     * @throws CannotValidateEmail if the user is not found or email validation fails
     */
    public ValidateEmailResponse validate(ValidateEmailRequest request) {
        try {
            this.userRepository.validateEmail(request.userId());
            return new ValidateEmailResponse();
        } catch (UserNotFoundException e) {
            throw new CannotValidateEmail();
        }
    }
}
