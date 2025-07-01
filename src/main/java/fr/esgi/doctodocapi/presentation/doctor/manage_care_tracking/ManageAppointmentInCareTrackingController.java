package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.book_appointment_in_care_tracking.BookedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.book_appointment_in_care_tracking.IBookAppointmentInCareTracking;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageAppointmentInCareTrackingController {
    private final IBookAppointmentInCareTracking bookAppointment;

    public ManageAppointmentInCareTrackingController(IBookAppointmentInCareTracking bookAppointment) {
        this.bookAppointment = bookAppointment;
    }

    @PostMapping("care-tracking/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public BookedAppointmentResponse bookAppointment(@Valid @RequestBody BookAppointmentRequest request) {
        return this.bookAppointment.execute(request);
    }
}
