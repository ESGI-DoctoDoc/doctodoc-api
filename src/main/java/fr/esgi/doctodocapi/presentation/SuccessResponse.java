package fr.esgi.doctodocapi.presentation;

public record SuccessResponse(
        boolean success,
        Object data
) {
}
