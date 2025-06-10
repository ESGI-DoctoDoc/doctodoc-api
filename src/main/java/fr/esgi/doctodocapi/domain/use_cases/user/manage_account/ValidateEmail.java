package fr.esgi.doctodocapi.domain.use_cases.user.manage_account;

import fr.esgi.doctodocapi.domain.use_cases.exceptions.CannotValidateEmail;
import fr.esgi.doctodocapi.domain.entities.user.UserNotFoundException;
import fr.esgi.doctodocapi.domain.entities.user.UserRepository;
import fr.esgi.doctodocapi.domain.use_cases.user.ports.in.IValidateEmail;

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
     * @param userId UUID of the user whose email must be validated
     * @throws CannotValidateEmail if the user is not found or email validation fails
     */
    public void validate(UUID userId) {
        try {
            this.userRepository.validateEmail(userId);
        } catch (UserNotFoundException e) {
            throw new CannotValidateEmail();
        }
    }
}
