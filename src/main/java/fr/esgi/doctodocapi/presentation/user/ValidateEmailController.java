package fr.esgi.doctodocapi.presentation.user;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateEmailRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.ValidateEmailResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IValidateEmail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for validating user email addresses.
 * Exposes an endpoint to validate a user's email via a userId parameter.
 */
@RestController
@RequestMapping("/users")
public class ValidateEmailController {

    private final IValidateEmail validateEmail;

    /**
     * Constructor injecting the ValidateEmail use case.
     *
     * @param validateEmail the service to handle email validation
     */
    public ValidateEmailController(IValidateEmail validateEmail) {
        this.validateEmail = validateEmail;
    }

    /**
     * Endpoint to validate the email of a user identified by userId.
     *
     * @param request with the UUID of the user whose email is to be validated
     */
    @PostMapping("validate-email")
    @ResponseStatus(value = HttpStatus.OK)
    public ValidateEmailResponse validateEmail(@RequestBody ValidateEmailRequest request) {
        return this.validateEmail.validate(request);
    }
}
