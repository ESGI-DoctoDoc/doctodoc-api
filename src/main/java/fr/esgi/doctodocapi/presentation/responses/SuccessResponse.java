package fr.esgi.doctodocapi.presentation.responses;

public record SuccessResponse(
        boolean success,
        Object data
) {
}
