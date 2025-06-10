package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class MedicalConcernNotAuthorizedException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "medical-concern.not-authorized";
    private static final String MESSAGE = "Le motif de consultation n'est pas autorisé dans le slot.";

    public MedicalConcernNotAuthorizedException() {
        super(STATUS, CODE, MESSAGE);
    }
}
