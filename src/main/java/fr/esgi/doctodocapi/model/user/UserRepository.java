package fr.esgi.doctodocapi.model.user;

import java.util.UUID;

public interface UserRepository {
    User findByEmail(String email) throws UserNotFoundException;
    void updateDoubleAuthCode(String code, UUID userId) throws UserNotFoundException;
}
