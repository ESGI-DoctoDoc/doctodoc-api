package fr.esgi.doctodocapi.presentation.patient.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.CancelAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void cancelAppointment(@PathVariable UUID id, @Valid @RequestBody CancelAppointmentRequest cancelAppointmentRequest) {
        this.cancelAppointment.cancel(id, cancelAppointmentRequest);
    }
}
