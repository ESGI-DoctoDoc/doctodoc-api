package fr.esgi.doctodocapi.domain.use_cases.user.ports.out;

/**
 * Port interface to retrieve information about the current request context.
 */
public interface GetCurrentRequestContext {

    /**
     * Returns the domain name of the current request.
     *
     * @return the current domain as a String, or an empty String if unavailable.
     */
    String getCurrentDomain();
}
