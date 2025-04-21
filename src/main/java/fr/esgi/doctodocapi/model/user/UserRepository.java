package fr.esgi.doctodocapi.model.user;

public interface UserRepository {
    User findByEmail(String email) throws UserNotFoundException;
    void updateDoubleAuthCode(String code, User user);
}
