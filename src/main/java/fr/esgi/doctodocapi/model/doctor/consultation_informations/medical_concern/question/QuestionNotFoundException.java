package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

import fr.esgi.doctodocapi.model.DomainException;

public class QuestionNotFoundException extends DomainException {
    private static final String CODE = "question.not-found";
    private static final String MESSAGE = "La question n'existe pas.";

    public QuestionNotFoundException() {
        super(CODE, MESSAGE);
    }
}
