package fr.esgi.doctodocapi.exceptions;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public AuthenticationException(AuthentificationMessageException authentificationMessageException) {
        super(STATUS, authentificationMessageException.getMessage());
    }
}
