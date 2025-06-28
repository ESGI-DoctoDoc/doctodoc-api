package fr.esgi.doctodocapi.presentation.patient.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class CancelAppointmentController {
    private final ICancelAppointment cancelAppointment;

    public CancelAppointmentController(ICancelAppointment cancelAppointment) {
        this.cancelAppointment = cancelAppointment;
    }

    @DeleteMapping("patients/appointments/cancel/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable UUID id) {
        this.cancelAppointment.cancel(id);
    }
}
