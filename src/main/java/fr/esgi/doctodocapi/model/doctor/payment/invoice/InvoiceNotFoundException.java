package fr.esgi.doctodocapi.model.doctor.payment.invoice;

import fr.esgi.doctodocapi.model.DomainException;

public class InvoiceNotFoundException extends DomainException {
    private static final String CODE = "invoice.not-found";
    private static final String MESSAGE = "La facture n'existe pas.";

    public InvoiceNotFoundException() {
        super(CODE, MESSAGE);
    }
}
