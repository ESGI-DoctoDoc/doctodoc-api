package fr.esgi.doctodocapi.model.user;

import java.util.UUID;

public interface UserRepository {
    User findByEmailOrPhoneNumber(String email, String phoneNumber) throws UserNotFoundException;
    void updateDoubleAuthCode(String code, UUID userId) throws UserNotFoundException;
    User findByEmail(String email) throws UserNotFoundException;

    void validateEmail(UUID userId) throws UserNotFoundException;
}
