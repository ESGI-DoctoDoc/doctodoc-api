package fr.esgi.doctodocapi.infrastructure.brevo;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class IcsContent {
    public static String build(String subject, String description, Invitation invitation) {
        ZoneId inputZone = ZoneId.of("Europe/Paris");
        ZoneId utc = ZoneId.of("UTC");

        ZonedDateTime start = invitation.start().atZone(inputZone).withZoneSameInstant(utc);
        ZonedDateTime end = invitation.end().atZone(inputZone).withZoneSameInstant(utc);
        ZonedDateTime now = ZonedDateTime.now(inputZone).withZoneSameInstant(utc);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        return "BEGIN:VCALENDAR\n" +
                "METHOD:REQUEST\n" +
                "PRODID:JavaMail\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:" + formatter.format(now) + "\n" +
                "DTSTART:" + formatter.format(start) + "\n" +
                "DTEND:" + formatter.format(end) + "\n" +
                "SUMMARY:" + subject + "\n" +
                "DESCRIPTION:" + description + "\n" +
                "LOCATION:" + invitation.location() + "\n" +
                "UID:" + UUID.randomUUID() + "\n" +
                "SEQUENCE:0\n" +
                "STATUS:CONFIRMED\n" +
                "TRANSP:OPAQUE\n" +
                "ORGANIZER;CN=Dr " + invitation.organizer().firstName() + invitation.organizer().lastName() + ":mailto:" + invitation.organizer().email() + "\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";
    }

}
