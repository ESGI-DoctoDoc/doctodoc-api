package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account;

import java.math.BigDecimal;

public record AddressProfileInfo(
        String formatted,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
