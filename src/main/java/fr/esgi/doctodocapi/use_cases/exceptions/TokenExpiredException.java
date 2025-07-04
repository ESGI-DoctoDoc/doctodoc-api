package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String CODE = "token.expired";
    private static final String MESSAGE = "Votre session a expiré. Veuillez vous reconnecter pour continuer.";

    public TokenExpiredException() {
        super(STATUS, CODE, MESSAGE);
    }
}
