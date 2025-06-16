package fr.esgi.doctodocapi.exceptions.on_boarding;

import fr.esgi.doctodocapi.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class HasAlreadyMainAccount extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "account.already-exist";
    private static final String MESSAGE = "Has already a main account.";

    public HasAlreadyMainAccount() {
        super(STATUS, CODE, MESSAGE);
    }
}
