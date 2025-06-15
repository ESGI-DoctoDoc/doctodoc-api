package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentStatusNotFound;

public enum AppointmentDoctorStatus {
    UPCOMING("upcoming"),
    CANCELLED_EXCUSED("cancelled-excused"),
    CANCELLED_UNEXCUSED("cancelled-unexcused"),
    COMPLETED("completed"),
    WAITING_ROOM("waiting-room");

    private final String value;

    AppointmentDoctorStatus(String value) {
        this.value = value;
    }

    public static AppointmentDoctorStatus fromValue(String value) {
        for (AppointmentDoctorStatus status : AppointmentDoctorStatus.values()) {
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