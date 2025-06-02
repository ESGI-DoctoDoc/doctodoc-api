package fr.esgi.doctodocapi.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String CODE = "unauthorized";
    private static final String MESSAGE = "unauthorized access to api";

    public UnauthorizedException() {
        super(STATUS, CODE, MESSAGE);
    }
}
