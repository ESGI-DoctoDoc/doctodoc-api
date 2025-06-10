package fr.esgi.doctodocapi.domain.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class CannotValidateEmail extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "account.cannot-validate-email";
    private static final String MESSAGE = "Cannot validate email";

    public CannotValidateEmail() {
        super(STATUS, CODE, MESSAGE);
    }
}
