package fr.esgi.doctodocapi.model.document;

public enum DocumentType {
    MEDICAL_REPORT("Rapport médical"),
    PRESCRIPTION("Ordonnance"),
    MEDICAL_CERTIFICATE("Certificat médical"),
    ANALYSES_RESULT("Résultats d'analyses"),
    PROFILE_PICTURE("photo de profil"),
    IDENTITY_FILE("justificatif d'identité"),
    OTHER("Autre");

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
