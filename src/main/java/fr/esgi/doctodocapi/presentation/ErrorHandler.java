package fr.esgi.doctodocapi.presentation;

import fr.esgi.doctodocapi.dtos.responses.ErrorResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ApiException exception = ApiException.from(ex);
        HttpStatus status = exception.getStatus();
        ErrorResponse errorResponse = new ErrorResponse(status.name(), status.value(), exception.getClass().getSimpleName(), exception.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
