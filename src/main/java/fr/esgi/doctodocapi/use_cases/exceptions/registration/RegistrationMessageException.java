package fr.esgi.doctodocapi.use_cases.exceptions.registration;

public enum RegistrationMessageException {
    SAVE_USER_FAILED("registration.save.failed", "Échec de l'enregistrement de l'utilisateur"),
    ;

    private final String code;
    private final String message;

    RegistrationMessageException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}