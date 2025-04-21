package fr.esgi.doctodocapi.use_cases.patient;

public class PatientNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Patient not found";

    public PatientNotFoundException() {
        super(MESSAGE);
    }
}
