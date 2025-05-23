package fr.esgi.doctodocapi.exceptions.authentication;

public enum AuthentificationMessageException {
    BAD_CREDENTIALS("credentials.incorrect", "Erreur d'authentification. Les identifiants sont incorrects."),
    ACCOUNT_NOT_ACTIVATED("account.not-activated", "Le compte n'a pas été activé. Nous vous avons envoyé un mail."),
    BAD_DOUBLE_AUTH_CODE("code.incorrect", "Erreur d'authentification. Le code est incorrect."),
    EMAIL_NOT_FOUND("email.not-found", "Aucun compte n'est associé à cet email."),
    INVALID_TOKEN("token.invalid", "Le lien de réinitialisation est invalide ou expiré."),
    RESET_PASSWORD_ERROR("reset-password.error", "Une erreur est survenue lors de la réinitialisation du mot de passe.")
    ;

    private final String code;
    private final String message;

    AuthentificationMessageException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
