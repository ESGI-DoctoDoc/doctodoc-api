package fr.esgi.doctodocapi.model.notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

public final class NotificationsType {

    private static final Locale LOCALE_FR = Locale.FRANCE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withLocale(LOCALE_FR);

    private static final String TITLE_APPOINTMENT_NEW = "Nouveau rendez-vous";
    private static final String TITLE_APPOINTMENT_CANCELED = "Rendez-vous annulé";
    private static final String TITLE_CARE_TRACKING = "Suivi de dossier";

    private NotificationsType() {
    }

    // -------------------------------
    // Appointments
    // -------------------------------

    public static Notification newAppointment(UUID recipientId, LocalDate date, LocalTime startTime, String patientFullName) {
        String message = String.format(
                "Vous avez un nouveau rendez-vous le %s à %s avec %s",
                formatDate(date),
                formatTime(startTime),
                patientFullName
        );
        return Notification.init(recipientId, TITLE_APPOINTMENT_NEW, message);
    }

    public static Notification cancelAppointment(UUID recipientId, LocalDate date, LocalTime startTime, String patientFullName) {
        String message = String.format(
                "Le rendez-vous avec %s le %s à %s a été annulé",
                patientFullName,
                formatDate(date),
                formatTime(startTime)
        );
        return Notification.init(recipientId, TITLE_APPOINTMENT_CANCELED, message);
    }

    // -------------------------------
    // Care Tracking
    // -------------------------------

    public static Notification addInCareTracking(UUID recipientId, String careTrackingName) {
        String message = String.format("Vous avez été ajouté à un nouveau suivi de dossier %s", careTrackingName);
        return Notification.init(recipientId, TITLE_CARE_TRACKING, message);
    }

    public static Notification newDocumentsInCareTracking(UUID recipientId, String careTrackingName) {
        String message = String.format("De nouveaux documents sont disponibles dans le suivi de dossier %s", careTrackingName);
        return Notification.init(recipientId, TITLE_CARE_TRACKING, message);
    }

    public static Notification newMessageInCareTracking(UUID recipientId, String careTrackingName) {
        String message = String.format("De nouveaux messages sont disponibles dans le suivi de dossier %s", careTrackingName);
        return Notification.init(recipientId, TITLE_CARE_TRACKING, message);
    }

    public static Notification careTrackingClose(UUID recipientId, String careTrackingName) {
        String message = String.format("Le suivi de dossier %s a été clos", careTrackingName);
        return Notification.init(recipientId, TITLE_CARE_TRACKING, message);
    }

    // -------------------------------
    // Private helpers
    // -------------------------------

    private static String formatDate(LocalDate date) {
        return String.format(
                "%s %d %s %d",
                date.getDayOfWeek().getDisplayName(TextStyle.FULL, LOCALE_FR),
                date.getDayOfMonth(),
                date.getMonth().getDisplayName(TextStyle.FULL, LOCALE_FR),
                date.getYear()
        );
    }

    private static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }
}
