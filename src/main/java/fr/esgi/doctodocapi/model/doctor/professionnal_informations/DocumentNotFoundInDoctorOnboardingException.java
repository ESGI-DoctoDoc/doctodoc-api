package fr.esgi.doctodocapi.model.doctor.professionnal_informations;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentNotFoundInDoctorOnboardingException extends DomainException {
    private static final String CODE = "document.onboarding.not-found";
    private static final String MESSAGE = "Le document n'existe pas dans l'onboarding du médecin.";

    public DocumentNotFoundInDoctorOnboardingException() {
        super(CODE, MESSAGE);
    }
}