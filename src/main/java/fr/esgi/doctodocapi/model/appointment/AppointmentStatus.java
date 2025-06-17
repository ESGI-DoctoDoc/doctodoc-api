package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentStatusNotFound;

public enum AppointmentStatus {
    UPCOMING("upcoming"),
    CONFIRMED("confirmed"),
    LOCKED("locked"),
    CANCELLED("cancelled-excused"),
    CANCELLED_UNEXCUSED("cancelled-unexcused"),
    COMPLETED("completed"),
    WAITING_ROOM("waiting-room");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    public static AppointmentStatus fromValue(String value) {
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new AppointmentStatusNotFound();
    }

    public String getValue() {
        return value;
    }
}
