package fr.esgi.doctodocapi.presentation.patient.notifications;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetNotificationResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IGetNotifications;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients/notifications")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetNotificationsController {
    private final IGetNotifications getNotifications;

    public GetNotificationsController(IGetNotifications getNotifications) {
        this.getNotifications = getNotifications;
    }

    @GetMapping
    public List<GetNotificationResponse> get() {
        return this.getNotifications.getSentInLastWeek();
    }
}
