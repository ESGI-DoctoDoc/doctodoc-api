package fr.esgi.doctodocapi.error.exceptions;

public class PatientNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Patient not found";

    public PatientNotFoundException() {
        super(MESSAGE);
    }
}
