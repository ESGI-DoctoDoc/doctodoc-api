package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account;

import java.math.BigDecimal;

public record FetchCoordinatesResponse(BigDecimal latitude, BigDecimal longitude) {}
