package fr.esgi.doctodocapi.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "user.already.exist";
    private static final String MESSAGE = "User already exist";

    public UserAlreadyExistException() {
        super(STATUS, CODE, MESSAGE);
    }
}
