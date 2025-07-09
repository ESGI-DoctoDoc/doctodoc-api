package fr.esgi.doctodocapi.presentation.patient.notifications;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IMarkReadOnNotification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients/notifications")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class MarkNotificationsAsReadController {
    private final IMarkReadOnNotification markReadOnNotification;

    public MarkNotificationsAsReadController(IMarkReadOnNotification markReadOnNotification) {
        this.markReadOnNotification = markReadOnNotification;
    }

    @PatchMapping("{id}")
    public void get(@PathVariable UUID id) {
        this.markReadOnNotification.process(id);
    }
}
