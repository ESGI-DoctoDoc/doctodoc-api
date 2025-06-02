package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentStatusNotFound;

public enum AppointmentStatus {
    CONFIRMED("confirmé"),
    LOCKED("bloqué"),
    CANCELLED("annulé");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    public static AppointmentStatus fromValue(String value) {
        for (AppointmentStatus type : AppointmentStatus.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new AppointmentStatusNotFound();
    }


    public String getValue() {
        return value;
    }
}
