package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentResponse;
import fr.esgi.doctodocapi.domain.use_cases.patient.make_appointment.GetAppointments;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients/appointments")
public class GetAppointmentsController {
    private final GetAppointments getAppointments;

    public GetAppointmentsController(GetAppointments getAppointments) {
        this.getAppointments = getAppointments;
    }

    @GetMapping(value = "/most-recent-upcoming")
    @ResponseStatus(value = HttpStatus.OK)
    public GetAppointmentResponse getMostRecentUpcomingAppointment() {
        return this.getAppointments.getMostRecentUpcomingAppointment();
    }

    @GetMapping(value = "/get-all-past")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAppointmentResponse> getAllPastAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getAppointments.getAllPastAppointments(page, size);
    }

    @GetMapping(value = "/get-all-upcoming")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAppointmentResponse> getAllUpcomingAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getAppointments.getAllUpcomingAppointments(page, size);
    }
}
