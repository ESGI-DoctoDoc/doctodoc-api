package fr.esgi.doctodocapi.presentation.doctor.notifications;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.MarkAsReadDoctorNotificationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IMarkDoctorNotificationAsRead;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class MarkAsReadDoctorNotificationController {
    private final IMarkDoctorNotificationAsRead markDoctorNotificationAsRead;

    public MarkAsReadDoctorNotificationController(IMarkDoctorNotificationAsRead markDoctorNotificationAsRead) {
        this.markDoctorNotificationAsRead = markDoctorNotificationAsRead;
    }

    @PatchMapping("/notifications/{id}/read")
    @ResponseStatus(value = HttpStatus.OK)
    public MarkAsReadDoctorNotificationResponse markAsRead(@PathVariable UUID id) {
        return this.markDoctorNotificationAsRead.process(id);
    }
}
