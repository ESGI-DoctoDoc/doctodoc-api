package fr.esgi.doctodocapi.use_cases.user.ports.in;

/**
 * Port interface to retrieve information about the current authenticated user.
 */
public interface GetCurrentUserContext {

    /**
     * Returns the username (usually email) of the current authenticated user.
     *
     * @return the username as a String.
     */
    String getUsername();

    /**
     * Returns the role of the current authenticated user.
     *
     * @return the user role as a String.
     */
    String getRole();
}
