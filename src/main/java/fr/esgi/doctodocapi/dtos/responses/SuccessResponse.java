package fr.esgi.doctodocapi.dtos.responses;

public record SuccessResponse(
        String status,
        int code,
        Object data
) {
}
