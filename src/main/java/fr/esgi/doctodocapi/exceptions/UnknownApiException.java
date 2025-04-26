package fr.esgi.doctodocapi.exceptions;

import org.springframework.http.HttpStatus;

public class UnknownApiException extends ApiException {
    private static final String MESSAGE = "Une erreur inconnue est survenue. ";
    private static final String CODE = "server.internal-error";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public UnknownApiException(String cause) {
        super(STATUS, CODE, MESSAGE + cause);
    }
}
