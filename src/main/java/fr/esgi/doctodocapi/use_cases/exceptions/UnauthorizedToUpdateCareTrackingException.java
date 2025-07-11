package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedToUpdateCareTrackingException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String CODE = "care-tracking.update-unauthorized";
    private static final String MESSAGE = "unauthorized for updating care tracking";

    public UnauthorizedToUpdateCareTrackingException() {
        super(STATUS, CODE, MESSAGE);
    }
}
