package fr.esgi.doctodocapi.presentation;

import fr.esgi.doctodocapi.presentation.responses.ErrorResponse;
import fr.esgi.doctodocapi.presentation.responses.SuccessResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Intercepts all controller responses to wrap successful responses
 * in a standardized SuccessResponse object.
 * <p>
 * Leaves ErrorResponse objects untouched to preserve error formatting.
 */
@ControllerAdvice
public class SuccessResponseWrapper implements ResponseBodyAdvice<Object> {

    /**
     * Indicates that this advice supports all controller methods.
     *
     * @param returnType    the return type of the controller method
     * @param converterType the selected converter type
     * @return true to apply beforeBodyWrite to all responses
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Wraps the response body into a SuccessResponse if it's not an ErrorResponse.
     *
     * @param body                  the original response body
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected for the response
     * @param selectedConverterType the converter type used to serialize the response
     * @param request               the server HTTP request
     * @param response              the server HTTP response
     * @return the original ErrorResponse or a wrapped SuccessResponse
     */
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body instanceof ErrorResponse) {
            return body;
        }
        return new SuccessResponse(true, body);
    }
}
