package fr.esgi.doctodocapi.presentation.doctor.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetDoctorAppointments;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageDoctorAppointmentsController {

    private final IGetDoctorAppointments getDoctorAppointments;

    public ManageDoctorAppointmentsController(IGetDoctorAppointments getDoctorAppointments) {
        this.getDoctorAppointments = getDoctorAppointments;
    }

    @GetMapping("appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorAppointmentResponse> getAllAppointments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorAppointments.execute(page, size);
    }
}