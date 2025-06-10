package fr.esgi.doctodocapi.domain.entities.user;

/**
 * Interface for generating two-factor authentication (2FA) codes.
 * <p>
 * Implementations must provide a method to generate a double authentication code,
 * typically used for enhancing security with two-factor authentication.
 */
public interface DoubleAuthCodeGenerator {

    /**
     * Generates a new two-factor authentication code as a String.
     *
     * @return a generated double authentication code
     */
    String generateDoubleAuthCode();
}
