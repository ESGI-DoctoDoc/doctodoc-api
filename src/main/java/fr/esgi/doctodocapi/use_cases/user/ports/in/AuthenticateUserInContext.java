package fr.esgi.doctodocapi.use_cases.user.ports.in;

public interface AuthenticateUserInContext {
    void persistAuthentication(String username, String password);
}
