package fr.esgi.doctodocapi.domain.use_cases.exceptions.authentication;

import fr.esgi.doctodocapi.domain.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public AuthenticationException(AuthentificationMessageException authentificationMessageException) {
        super(STATUS, authentificationMessageException.getCode(), authentificationMessageException.getMessage());
    }
}
