package fr.esgi.doctodocapi.presentation.admin.notifications;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.MarkAsReadAdminNotificationResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IMarkAdminNotificationAsRead;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MarkAsReadAdminNotificationController {
    private final IMarkAdminNotificationAsRead markAdminNotificationAsRead;

    public MarkAsReadAdminNotificationController(IMarkAdminNotificationAsRead markAdminNotificationAsRead) {
        this.markAdminNotificationAsRead = markAdminNotificationAsRead;
    }

    @PatchMapping("/notifications/{id}/read")
    @ResponseStatus(value = HttpStatus.OK)
    public MarkAsReadAdminNotificationResponse markAsRead(@PathVariable UUID id) {
        return this.markAdminNotificationAsRead.process(id);
    }
}
