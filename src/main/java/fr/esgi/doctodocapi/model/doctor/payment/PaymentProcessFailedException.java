package fr.esgi.doctodocapi.model.doctor.payment;

import fr.esgi.doctodocapi.model.DomainException;

public class PaymentProcessFailedException extends DomainException {
    private static final String CODE = "payment.process.failed";
    private static final String MESSAGE = "Le traitement du paiement a échoué.";

    public PaymentProcessFailedException() {
        super(CODE, MESSAGE);
    }
}