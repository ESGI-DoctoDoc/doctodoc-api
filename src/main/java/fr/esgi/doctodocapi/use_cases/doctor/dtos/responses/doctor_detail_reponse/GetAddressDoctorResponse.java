package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse;

import java.math.BigDecimal;

public record GetAddressDoctorResponse(
        String address,
        BigDecimal latitude,
        BigDecimal longitude

) {
}
