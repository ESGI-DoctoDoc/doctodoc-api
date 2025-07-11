package fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class NotificationMessageType {
    private static final Locale LOCALE_FR = Locale.FRANCE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withLocale(LOCALE_FR);
    private static final String TITLE_CARE_TRACKING = "Suivi de dossier";

    private NotificationMessageType() {
    }

    // -------------------------------
    // Care Tracking
    // -------------------------------

    public static NotificationMessage closeCareTracking(UUID recipientId, UUID careTrackingId, String careTrackingName, LocalDateTime closedAt, String doctorFullName) {
        String message = String.format(
                "Le médecin %s a clos votre suivi de dossier '%s' le %s.",
                doctorFullName,
                careTrackingName,
                formatDateTime(closedAt)
        );
        return new NotificationMessage(
                recipientId,
                TITLE_CARE_TRACKING,
                message,
                Map.of("careTracking_id", careTrackingId.toString())
        );
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        String datePart = formatDate(dateTime.toLocalDate());
        String timePart = formatTime(dateTime.toLocalTime());
        return datePart + " à " + timePart;
    }


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
