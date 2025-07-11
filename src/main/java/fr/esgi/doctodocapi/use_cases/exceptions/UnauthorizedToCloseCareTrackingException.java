package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedToCloseCareTrackingException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String CODE = "care-tracking.close-unauthorized";
    private static final String MESSAGE = "unauthorized for closing care tracking";

    public UnauthorizedToCloseCareTrackingException() {
        super(STATUS, CODE, MESSAGE);
    }
}
