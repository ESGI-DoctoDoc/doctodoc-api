package fr.esgi.doctodocapi.exceptions.registration;

import fr.esgi.doctodocapi.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RegistrationException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public RegistrationException(RegistrationMessageException registrationMessageException) {
        super(STATUS, registrationMessageException.getCode(), registrationMessageException.getMessage());
    }
}