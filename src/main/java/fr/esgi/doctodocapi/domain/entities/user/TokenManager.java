package fr.esgi.doctodocapi.domain.entities.user;

/**
 * Interface responsible for managing authentication tokens.
 * <p>
 * Implementations must provide methods to generate tokens for a given user
 * (including username, role, and expiration) and to extract token details
 * (such as role and username) from an existing token string.
 * </p>
 */
public interface TokenManager {

    /**
     * Generates an authentication token.
     *
     * @param username            the username for whom the token is generated
     * @param role                the role of the user
     * @param expirationInMinutes the token expiration time in minutes
     * @return a generated authentication token as a String
     */
    String generate(String username, String role, Integer expirationInMinutes);

    /**
     * Extrait le rôle stocké dans un token existant.
     *
     * @param token le token d’authentification encodé
     * @return la valeur du rôle (claim) contenue dans le token, ou {@code null} si le rôle est absent ou invalide
     */
    String extractRole(String token);

    /**
     * Extrait le nom d’utilisateur (username) stocké dans un token existant.
     *
     * @param token le token d’authentification encodé
     * @return la valeur du nom d’utilisateur (claim) contenue dans le token, ou {@code null} si le nom est absent ou invalide
     */
    String extractUserName(String token);
}
