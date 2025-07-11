package fr.esgi.doctodocapi.model.user;

import java.util.UUID;

/**
 * Repository interface for User persistence operations.
 * Defines methods to find, save, update and validate users.
 */
public interface UserRepository {

    /**
     * Finds a user by email or phone number.
     *
     * @param email       the user's email to search for
     * @param phoneNumber the user's phone number to search for
     * @return the User matching the email or phone number
     * @throws UserNotFoundException if no matching user is found
     */
    User findByEmailOrPhoneNumber(String email, String phoneNumber) throws UserNotFoundException;

    /**
     * Updates the two-factor authentication code for a user.
     *
     * @param code   the new 2FA code to set
     * @param userId the UUID of the user to update
     * @throws UserNotFoundException if the user does not exist
     */
    void updateDoubleAuthCode(String code, UUID userId) throws UserNotFoundException;

    /**
     * Finds a user by email.
     *
     * @param email the user's email
     * @return the User with the specified email
     * @throws UserNotFoundException if no user with the email exists
     */
    User findByEmail(String email) throws UserNotFoundException;

    User findById(UUID id) throws UserNotFoundException;

    /**
     * Saves a user entity.
     *
     * @param user the User to save
     * @return the saved User instance
     */
    User save(User user);

    /**
     * Checks if a user exists with the given email or phone number.
     *
     * @param email       the email to check
     * @param phoneNumber the phone number to check
     * @return true if a user exists, false otherwise
     */
    boolean isExistUser(String email, String phoneNumber);

    /**
     * Marks the email of the user as validated.
     *
     * @param userId the UUID of the user
     * @throws UserNotFoundException if the user does not exist
     */
    void validateEmail(UUID userId) throws UserNotFoundException;
    void updatePassword(User user) throws UserNotFoundException;

    void changePassword(UUID userId, String newPassword, String oldPassword) throws InvalidPassword, UserNotFoundException;
}
