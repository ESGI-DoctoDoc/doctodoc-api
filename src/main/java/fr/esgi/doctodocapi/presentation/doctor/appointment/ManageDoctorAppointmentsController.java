package fr.esgi.doctodocapi.presentation.doctor.appointment;

import fr.esgi.doctodocapi.dtos.responses.doctor.appointment.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.appointment.GetDoctorAppointments;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageDoctorAppointmentsController {

    private final GetDoctorAppointments getDoctorAppointments;

    public ManageDoctorAppointmentsController(GetDoctorAppointments getDoctorAppointments) {
        this.getDoctorAppointments = getDoctorAppointments;
    }

    @GetMapping("appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorAppointmentResponse> getAllAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorAppointments.execute(page, size);
    }
}