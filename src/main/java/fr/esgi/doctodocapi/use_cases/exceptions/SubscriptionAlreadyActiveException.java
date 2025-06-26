package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyActiveException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "subscription.already-active";
    private static final String MESSAGE = "Vous avez déjà un abonnement en cours.";

    public SubscriptionAlreadyActiveException() {
        super(STATUS, CODE, MESSAGE);
    }
}