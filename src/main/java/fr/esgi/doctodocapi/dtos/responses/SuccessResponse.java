package fr.esgi.doctodocapi.dtos.responses;

public record SuccessResponse(
        boolean success,
        Object data
) {
}
