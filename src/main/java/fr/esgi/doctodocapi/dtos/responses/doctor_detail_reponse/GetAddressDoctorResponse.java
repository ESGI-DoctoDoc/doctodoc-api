package fr.esgi.doctodocapi.dtos.responses.doctor_detail_reponse;

import java.math.BigDecimal;

public record GetAddressDoctorResponse(
        String address,
        BigDecimal latitude,
        BigDecimal longitude

) {
}
