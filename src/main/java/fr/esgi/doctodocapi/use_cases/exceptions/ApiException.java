package fr.esgi.doctodocapi.use_cases.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static ApiException from(Exception exception) {
        return switch (exception) {
            case ApiException apiException -> apiException;
            case MethodArgumentNotValidException methodArgumentNotValidException ->
                    ValidationException.of(methodArgumentNotValidException);
            default -> new UnknownApiException(exception.getMessage());
        };
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}