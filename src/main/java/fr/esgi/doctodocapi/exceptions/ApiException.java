package fr.esgi.doctodocapi.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ApiException from(Exception exception) {
        if (exception instanceof ApiException apiException) {
            return apiException;
        }
//        else if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
//            String message = ValidationException.getMessageFromException(methodArgumentNotValidException);
//            return new ValidationException(message);
//        }
        else {
            return new UnknownApiException(exception.getMessage());
        }

    }

    public HttpStatus getStatus() {
        return status;
    }

}
