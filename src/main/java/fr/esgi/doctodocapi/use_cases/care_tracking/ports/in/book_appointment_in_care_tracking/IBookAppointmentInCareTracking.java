package fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.book_appointment_in_care_tracking;

import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.book_appointment_in_care_tracking.BookedAppointmentResponse;

public interface IBookAppointmentInCareTracking {
    BookedAppointmentResponse execute(BookAppointmentRequest request);
}
