package fr.esgi.doctodocapi.model.document;

public enum DocumentType {
    PRESCRIPTION("ordonnance"),
    ANALYSE_RESULT("résultat d'analyse"),
    IMAGERY("imagerie"),
    VACCINE("vaccin");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromValue(String value) {
        for (DocumentType status : DocumentType.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new DocumentTypeNotFoundException();
    }

    public String getValue() {
        return value;
    }
}
