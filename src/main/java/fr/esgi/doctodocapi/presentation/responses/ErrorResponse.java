package fr.esgi.doctodocapi.presentation.responses;

public record ErrorResponse(
        String status,
        String code,
        String message
) {
}
