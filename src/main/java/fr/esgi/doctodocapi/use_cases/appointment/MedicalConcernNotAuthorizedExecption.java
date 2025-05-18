package fr.esgi.doctodocapi.use_cases.appointment;

import fr.esgi.doctodocapi.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class MedicalConcernNotAuthorizedExecption extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "medical-concern.not-authorized";
    private static final String MESSAGE = "Le motif de consultation n'est pas autorisé dans le slot.";

    public MedicalConcernNotAuthorizedExecption() {
        super(STATUS, CODE, MESSAGE);
    }
}
