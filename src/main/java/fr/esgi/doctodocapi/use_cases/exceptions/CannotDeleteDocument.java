package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class CannotDeleteDocument extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "document.cannot-delete-document";
    private static final String MESSAGE = "You're not the uploader so you can't delete document.";

    public CannotDeleteDocument() {
        super(STATUS, CODE, MESSAGE);
    }
}
