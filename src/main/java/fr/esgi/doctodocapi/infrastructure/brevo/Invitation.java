package fr.esgi.doctodocapi.infrastructure.brevo;

import java.time.LocalDateTime;

public record Invitation(
        Organizer organizer,
        String location,
        LocalDateTime start,
        LocalDateTime end
) {
}
