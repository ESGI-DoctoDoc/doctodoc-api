package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

import fr.esgi.doctodocapi.model.DomainException;

public class QuestionTypeNotFound extends DomainException {
    private static final String CODE = "question-type.not-found";
    private static final String MESSAGE = "Le type de question n'existe pas.";

    public QuestionTypeNotFound() {
        super(CODE, MESSAGE);
    }
}
