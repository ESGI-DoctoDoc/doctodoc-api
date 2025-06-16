package fr.esgi.doctodocapi.presentation;

public record ErrorResponse(
        String status,
        String code,
        String message
) {
}
