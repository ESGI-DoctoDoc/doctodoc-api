package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses;

import java.math.BigDecimal;

public record GetAppointmentAddressResponse(
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
