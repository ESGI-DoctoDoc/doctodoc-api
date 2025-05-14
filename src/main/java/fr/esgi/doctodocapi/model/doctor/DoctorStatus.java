package fr.esgi.doctodocapi.model.doctor;

public enum DoctorStatus {
    PENDING_VALIDATION("pending validation"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    DoctorStatus(String value) {
        this.value = value;
    }

    public static DoctorStatus fromValue(String value) {
        for (DoctorStatus type : DoctorStatus.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new DoctorStatusNotFoundException();
    }

    public String getValue() {
        return value;
    }
}