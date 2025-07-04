package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentDetailsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentsForAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetAppointmentController {
    private final IGetAppointmentsForAdmin getAppointments;
    private final IGetAppointmentDetailsForAdmin getAppointmentDetails;

    public GetAppointmentController(IGetAppointmentsForAdmin getAppointments, IGetAppointmentDetailsForAdmin getAppointmentDetails) {
        this.getAppointments = getAppointments;
        this.getAppointmentDetails = getAppointmentDetails;
    }

    @GetMapping("appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<GetAppointmentForAdminResponse> fetchAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return this.getAppointments.getDoctorAppointment(page, size);
    }


    @GetMapping("appointments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetAppointmentForAdminResponse fetchAppointmentById(@PathVariable UUID id) {
        return this.getAppointmentDetails.execute(id);
    }
}
