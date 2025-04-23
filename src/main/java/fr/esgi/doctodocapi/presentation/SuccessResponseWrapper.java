package fr.esgi.doctodocapi.presentation;

import fr.esgi.doctodocapi.dtos.responses.ErrorResponse;
import fr.esgi.doctodocapi.dtos.responses.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class SuccessResponseWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof ErrorResponse) {
            return body;
        }

        int statusCode = 200;

        if (response instanceof ServletServerHttpResponse servletResponse) {
            HttpServletResponse realResponse = servletResponse.getServletResponse();
            statusCode = realResponse.getStatus();
        }

        String statusName = HttpStatus.valueOf(statusCode).name();

        return new SuccessResponse(statusName, statusCode, body);
    }
}
