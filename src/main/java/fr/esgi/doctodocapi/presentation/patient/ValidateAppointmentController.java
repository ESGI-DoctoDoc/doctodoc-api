package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.save_appointment_request.SaveAppointmentRequest;
import fr.esgi.doctodocapi.dtos.responses.LockedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.appointment.ValidateAppointment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ValidateAppointmentController {
    private final ValidateAppointment validateAppointment;

    public ValidateAppointmentController(ValidateAppointment validateAppointment) {
        this.validateAppointment = validateAppointment;
    }

    @PostMapping("patients/appointments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LockedAppointmentResponse bookAppointment(@Valid @RequestBody SaveAppointmentRequest saveAppointmentRequest) {
        return this.validateAppointment.lock(saveAppointmentRequest);
    }

    @DeleteMapping("patients/appointments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void unlockedAppointment(@PathVariable UUID id) {
        this.validateAppointment.unlocked(id);
    }

    @PatchMapping("patients/appointments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmAppointment(@PathVariable UUID id) {
        this.validateAppointment.confirm(id);
    }
}
