package fr.esgi.doctodocapi.presentation;

import fr.esgi.doctodocapi.dtos.responses.ErrorResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ApiException exception = ApiException.from(ex);
        HttpStatus status = exception.getStatus();
        ErrorResponse errorResponse = new ErrorResponse(status.name(), exception.getCode(), exception.getMessage());
        logger.error("Error : code {}, message {}", errorResponse.code(), errorResponse.message());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
