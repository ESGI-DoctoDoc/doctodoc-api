package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentDetailedResponse(
        UUID id,
        String medicalConcern,
        double price,
        LocalDate date,
        LocalDateTime takenAt,
        LocalTime start,
        LocalTime end,
        GetAppointmentPatientResponse patient,
        GetAppointmentDoctorResponse doctor,
        GetAppointmentAddressResponse address
//        List<GetPreAppointmentAnswersResponse> responses
) {
}
