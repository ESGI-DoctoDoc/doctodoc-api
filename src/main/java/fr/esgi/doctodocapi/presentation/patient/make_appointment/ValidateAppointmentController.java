package fr.esgi.doctodocapi.presentation.patient.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.save_appointment.SaveAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.LockedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IValidateAppointment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing patient appointments:
 * booking (locking), unlocking, and confirming appointments.
 */
@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ValidateAppointmentController {
    private final IValidateAppointment validateAppointment;

    public ValidateAppointmentController(IValidateAppointment validateAppointment) {
        this.validateAppointment = validateAppointment;
    }

    /**
     * Books (locks) an appointment based on the patient's request.
     *
     * @param saveAppointmentRequest the appointment details to lock
     * @return response containing information about the locked appointment
     */
    @PostMapping("patients/appointments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LockedAppointmentResponse bookAppointment(@Valid @RequestBody SaveAppointmentRequest saveAppointmentRequest) {
        return this.validateAppointment.lock(saveAppointmentRequest);
    }

    /**
     * Unlocks (cancels) a previously locked appointment by ID.
     *
     * @param id the UUID of the appointment to unlock
     */
    @DeleteMapping("patients/appointments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void unlockedAppointment(@PathVariable UUID id) {
        this.validateAppointment.unlocked(id);
    }

    /**
     * Confirms a locked appointment by ID.
     *
     * @param id the UUID of the appointment to confirm
     */
    @PatchMapping("patients/appointments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmAppointment(@PathVariable UUID id) {
        this.validateAppointment.confirm(id);
    }
}
