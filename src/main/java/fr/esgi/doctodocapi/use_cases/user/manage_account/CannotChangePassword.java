package fr.esgi.doctodocapi.use_cases.user.manage_account;

import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class CannotChangePassword extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "account.cannot-change-password";
    private static final String MESSAGE = "Cannot change password.";

    public CannotChangePassword() {
        super(STATUS, CODE, MESSAGE);
    }
}
