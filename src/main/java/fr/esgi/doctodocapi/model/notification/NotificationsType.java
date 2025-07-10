package fr.esgi.doctodocapi.model.notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

public class NotificationsType {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.FRANCE);

    private NotificationsType() {
    }

    // appointments
    public static Notification newAppointment(UUID recipientId, LocalDate date, LocalTime start, String patientFullName) {
        String title = "Nouveau rendez-vous";
        String formattedDate = getFormattedDate(date);
        String formattedTime = start.format(TIME_FORMATTER);
        String message = String.format("Vous avez un nouveau rendez-vous le %s à %s avec %s", formattedDate, formattedTime, patientFullName);

        return Notification.init(recipientId, title, message);
    }

    public static Notification cancelAppointment(UUID recipientId, LocalDate date, LocalTime start, String patientFullName) {
        String title = "Rendez-vous annulé";
        String formattedDate = getFormattedDate(date);
        String formattedTime = start.format(TIME_FORMATTER);
        String message = String.format("Le rendez-vous avec %s le %s à %s a été annulé", patientFullName, formattedDate, formattedTime);

        return Notification.init(recipientId, title, message);
    }

    // care tracking
    public static Notification addInCareTracking(UUID recipientId, String careTrackingName) {
        String title = "Suivi de dossier";
        String message = String.format("Vous avez été ajouté à un nouveau suivi de dossier %s", careTrackingName);
        return Notification.init(recipientId, title, message);
    }

    public static Notification newDocumentsInCareTracking(UUID recipientId, String careTrackingName) {
        String title = "Suivi de dossier";
        String message = String.format("De nouveaux documents sont disponibles dans le suivi de dossier %s", careTrackingName);
        return Notification.init(recipientId, title, message);
    }

    // messages
    public static Notification newMessageInCareTracking(UUID recipientId, String careTrackingName) {
        String title = "Suivi de dossier";
        String message = String.format("Nouveaux messages disponibles dans le suivi du dossier %s", careTrackingName);
        return Notification.init(recipientId, title, message);
    }

    private static String getFormattedDate(LocalDate date) {
        Locale localeFr = Locale.FRANCE;
        return String.format(
                "%s %d %s %d",
                date.getDayOfWeek().getDisplayName(TextStyle.FULL, localeFr),
                date.getDayOfMonth(),
                date.getMonth().getDisplayName(TextStyle.FULL, localeFr),
                date.getYear()
        );
    }
}
