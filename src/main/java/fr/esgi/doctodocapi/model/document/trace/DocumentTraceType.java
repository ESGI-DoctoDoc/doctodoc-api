package fr.esgi.doctodocapi.model.document.trace;

public enum DocumentTraceType {
    ADD("Ajout"),
    DELETION("Suppression"),
    UPDATE("Modification"),
    ADD_PERMISSION("Ajout de permission"),
    REMOVE_PERMISSION("Suppression de permission");

    private final String value;

    DocumentTraceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
