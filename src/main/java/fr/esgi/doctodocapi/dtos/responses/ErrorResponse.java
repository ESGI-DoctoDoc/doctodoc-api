package fr.esgi.doctodocapi.dtos.responses;

public record ErrorResponse(
        String status,
        String code,
        String message
) {
}
