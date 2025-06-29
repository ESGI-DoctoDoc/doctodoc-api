package fr.esgi.doctodocapi.use_cases.doctor.ports.in.book_appointment_in_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.book_appointment_in_care_tracking.BookedAppointmentResponse;

public interface IBookAppointmentInCareTracking {
    BookedAppointmentResponse execute(BookAppointmentRequest request);
}
