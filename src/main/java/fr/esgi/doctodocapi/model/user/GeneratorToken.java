package fr.esgi.doctodocapi.model.user;

/**
 * Interface for generating authentication tokens.
 * <p>
 * Implementations should provide a method to generate a token based on user details
 * such as username, role, and token expiration time.
 */
public interface GeneratorToken {

    /**
     * Generates an authentication token.
     *
     * @param username            the username for whom the token is generated
     * @param role                the role of the user
     * @param expirationInMinutes the token expiration time in minutes
     * @return a generated authentication token as a String
     */
    String generate(String username, String role, Integer expirationInMinutes);
    String extractRole(String token);
    String extractUserName(String token);
}
