package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors;

import java.math.BigDecimal;

public record AddressInfo(
        String formatted,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
