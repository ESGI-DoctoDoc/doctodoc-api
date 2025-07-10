package fr.esgi.doctodocapi.presentation.doctor.notifications;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.GetDoctorNotificationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IGetDoctorNotifications;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetDoctorNotificationController {
    private final IGetDoctorNotifications getDoctorNotifications;

    public GetDoctorNotificationController(IGetDoctorNotifications getDoctorNotifications) {
        this.getDoctorNotifications = getDoctorNotifications;
    }

    @GetMapping("/notifications")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorNotificationResponse> getAll() {
        return this.getDoctorNotifications.getAll();
    }
}
