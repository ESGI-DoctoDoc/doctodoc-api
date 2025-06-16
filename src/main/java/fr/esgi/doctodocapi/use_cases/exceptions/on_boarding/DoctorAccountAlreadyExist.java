package fr.esgi.doctodocapi.use_cases.exceptions.on_boarding;

import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class DoctorAccountAlreadyExist extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "doctor-account.already-exist";
    private static final String MESSAGE = "A doctor account already exist with this id.";

    public DoctorAccountAlreadyExist() {
        super(STATUS, CODE, MESSAGE);
    }
}
