package fr.esgi.doctodocapi.use_cases.patient.ports.out;

import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class FileNotExistedException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String CODE = "medical-file.not-found";
    private static final String MESSAGE = "File doesn't exist.";

    public FileNotExistedException() {
        super(STATUS, CODE, MESSAGE);
    }
}
