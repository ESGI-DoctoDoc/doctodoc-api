package fr.esgi.doctodocapi.model.document.trace;

import fr.esgi.doctodocapi.model.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentTrace(
        UUID id,
        DocumentTraceType type,
        String description,
        User user,
        LocalDateTime date
) {
    public DocumentTrace {
        id = UUID.randomUUID();
        date = LocalDateTime.now();
    }
}
