package fr.esgi.doctodocapi.model.doctor.calendar.slot;

public enum RecurrenceType {
    NONE("none"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    private final String value;

    RecurrenceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RecurrenceType fromValue(String value) {
        for (RecurrenceType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new RecurrenceTypeNotFound();
    }
}
