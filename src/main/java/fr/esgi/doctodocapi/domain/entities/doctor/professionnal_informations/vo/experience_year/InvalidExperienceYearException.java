package fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.vo.experience_year;

import fr.esgi.doctodocapi.domain.DomainException;

public class InvalidExperienceYearException extends DomainException {
    private static final String CODE = "experience-year.invalid";
    private static final String MESSAGE = "le nombre d'année d'expérience ne peut être inférieur à zéro";
    public InvalidExperienceYearException() {
        super(CODE, MESSAGE);
    }
}
