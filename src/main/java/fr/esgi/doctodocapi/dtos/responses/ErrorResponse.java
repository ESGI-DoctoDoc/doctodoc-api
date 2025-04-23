package fr.esgi.doctodocapi.dtos.responses;

public record ErrorResponse(
        String status,
        int code,
        String error,
        String message
) {
}
