package fr.esgi.doctodocapi.domain.use_cases.user.ports.out;

/**
 * Port interface for authenticating a user within the current context.
 * Provides a method to persist user authentication data.
 */
public interface AuthenticateUserInContext {

    /**
     * Persists the authentication of a user given their username and password.
     *
     * @param username the user's login identifier (e.g., email)
     * @param password the user's password
     */
    void persistAuthentication(String username, String password);
}
