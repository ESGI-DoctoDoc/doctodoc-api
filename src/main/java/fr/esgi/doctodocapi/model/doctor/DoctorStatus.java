package fr.esgi.doctodocapi.model.doctor;

public enum DoctorStatus {
    PENDING_VALIDATION("pending validation"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    DoctorStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}