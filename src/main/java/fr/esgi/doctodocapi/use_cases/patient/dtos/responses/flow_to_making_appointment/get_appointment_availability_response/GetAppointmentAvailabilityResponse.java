package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.get_appointment_availability_response;

import java.time.LocalDate;
import java.util.List;

public record GetAppointmentAvailabilityResponse(
        List<GetTodayAppointmentAvailabilityResponse> today,
        LocalDate previous,
        LocalDate next
) {
}
