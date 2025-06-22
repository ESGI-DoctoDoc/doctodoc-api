package fr.esgi.doctodocapi.presentation.care_tracking;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.book_appointment_in_care_tracking.BookedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.book_appointment_in_care_tracking.IBookAppointmentInCareTracking;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors")
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
