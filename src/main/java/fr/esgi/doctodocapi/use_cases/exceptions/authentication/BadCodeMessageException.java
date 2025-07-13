package fr.esgi.doctodocapi.use_cases.exceptions.authentication;

import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class BadCodeMessageException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadCodeMessageException(AuthentificationMessageException authentificationMessageException) {
        super(STATUS, authentificationMessageException.getCode(), authentificationMessageException.getMessage());
    }
}
