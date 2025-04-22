package fr.esgi.doctodocapi.exceptions;

public enum AuthentificationMessageException {
    BAD_CREDENTIALS("Erreur d'authentification. Les identifiants sont incorrects."),
    ACCOUNT_NOT_ACTIVATED("Le compte n'a pas été activé. Nous vous avons envoyé un mail."),
    BAD_DOUBLE_AUTH_CODE("Erreur d'authentification. Le code est incorrect."),
    ;
    
    private final String message;

    AuthentificationMessageException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
