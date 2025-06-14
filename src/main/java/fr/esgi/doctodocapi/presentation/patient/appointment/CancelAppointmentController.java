package fr.esgi.doctodocapi.presentation.patient.appointment;

import fr.esgi.doctodocapi.use_cases.patient.appointment.CancelAppointment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CancelAppointmentController {
    private final CancelAppointment cancelAppointment;

    public CancelAppointmentController(CancelAppointment cancelAppointment) {
        this.cancelAppointment = cancelAppointment;
    }

    @DeleteMapping("patients/appointments/cancel/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable UUID id) {
        this.cancelAppointment.cancel(id);
    }
}
