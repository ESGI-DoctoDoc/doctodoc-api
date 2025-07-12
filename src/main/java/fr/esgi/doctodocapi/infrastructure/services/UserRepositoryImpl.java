package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.UserMapper;
import fr.esgi.doctodocapi.model.user.InvalidPassword;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Implementation of the UserRepository interface.
 * This service provides methods to manage user data, including user authentication,
 * registration, and profile management operations.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    /**
     * Repository for accessing user data in the database.
     */
    private final UserJpaRepository userJpaRepository;

    /**
     * Mapper for converting between user domain objects and entities.
     */
    private final UserMapper userMapper;

    /**
     * Encoder for hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserRepositoryImpl with the required repository, mapper, and password encoder.
     *
     * @param userJpaRepository Repository for user data access
     * @param userMapper        Mapper for user domain objects and entities
     * @param passwordEncoder   Encoder for hashing passwords
     */
    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a user by their email address or phone number.
     *
     * @param email The email address to search for
     * @param phoneNumber The phone number to search for
     * @return The user with the specified email or phone number
     * @throws UserNotFoundException If no user with the specified email or phone number exists
     */
    @Override
    public User findByEmailOrPhoneNumber(String email, String phoneNumber) {
        UserEntity userFoundByMail = this.userJpaRepository.findByEmailIgnoreCaseOrPhoneNumber(email, phoneNumber).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundByMail);
    }

    /**
     * Updates the double authentication code for a user.
     * This method also activates double authentication for the user.
     *
     * @param code The new double authentication code
     * @param userId The unique identifier of the user to update
     * @throws UserNotFoundException If no user with the specified ID exists
     */
    @Override
    public void updateDoubleAuthCode(String code, UUID userId) {
        UserEntity userEntity = this.userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userEntity.setDoubleAuthCode(code);
        userEntity.setDoubleAuthActive(true);
        this.userJpaRepository.save(userEntity);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return The user with the specified email address
     * @throws UserNotFoundException If no user with the specified email exists
     */
    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        UserEntity userFoundByMail = this.userJpaRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundByMail);
    }

    @Override
    public User findById(UUID id) throws UserNotFoundException {
        UserEntity userFoundById = this.userJpaRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundById);

    }


    /**
     * Saves a user to the database.
     * This method hashes the user's password before saving.
     *
     * @param user The user to save
     * @return The saved user with updated information
     */
    @Override
    public User save(User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword().getValue());
        UserEntity userSaved = this.userMapper.toEntity(user, hashPassword);
        this.userJpaRepository.save(userSaved);
        return this.userMapper.toDomain(userSaved);
    }

    @Override
    public void anonymise(User user) {
        UserEntity userFoundById = this.userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        userFoundById.setEmail(user.getEmail().getValue());
        userFoundById.setPhoneNumber(user.getPhoneNumber().getValue());
        this.userJpaRepository.save(userFoundById);
    }

    /**
     * Checks if a user with the specified email or phone number exists.
     *
     * @param email The email address to check
     * @param phoneNumber The phone number to check
     * @return true if a user with the specified email or phone number exists, false otherwise
     */
    @Override
    public boolean isExistUser(String email, String phoneNumber) {
        return this.userJpaRepository.findByEmailIgnoreCaseOrPhoneNumber(email, phoneNumber).isPresent();
    }

    /**
     * Validates the email address of a user.
     * This method marks the user's email as verified.
     *
     * @param userId The unique identifier of the user to validate
     * @throws UserNotFoundException If no user with the specified ID exists
     */
    @Override
    public void validateEmail(UUID userId) {
        UserEntity userFoundByMail = this.userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userFoundByMail.setEmailVerified(true);
        this.userJpaRepository.save(userFoundByMail);
    }

    @Override
    public void updatePassword(User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword().getValue());
        UserEntity userSaved = this.userMapper.toEntity(user, hashPassword);
        userSaved.setId(user.getId());
        this.userJpaRepository.save(userSaved);
    }

    @Override
    public void changePassword(UUID userId, String newPassword, String oldPassword) {
        UserEntity userFound = this.userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!this.passwordEncoder.matches(oldPassword, userFound.getPassword())) {
            throw new InvalidPassword();
        }
        String newHashPassword = this.passwordEncoder.encode(newPassword);

        userFound.setPassword(newHashPassword);
        this.userJpaRepository.save(userFound);
    }
}
