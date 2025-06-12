package fr.esgi.doctodocapi.dtos.responses.appointment_response;

import java.math.BigDecimal;

public record GetAppointmentAddressResponse(
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
