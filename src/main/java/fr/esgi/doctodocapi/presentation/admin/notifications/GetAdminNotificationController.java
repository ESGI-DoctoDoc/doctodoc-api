package fr.esgi.doctodocapi.presentation.admin.notifications;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.GetAdminNotificationResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IGetAdminNotifications;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetAdminNotificationController {
    private final IGetAdminNotifications getAdminNotifications;

    public GetAdminNotificationController(IGetAdminNotifications getAdminNotifications) {
        this.getAdminNotifications = getAdminNotifications;
    }

    @GetMapping("/notifications")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAdminNotificationResponse> getAll() {
        return this.getAdminNotifications.getAll();
    }
}
