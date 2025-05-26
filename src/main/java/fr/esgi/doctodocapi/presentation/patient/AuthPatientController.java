package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.patient.AuthenticatePatient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle patient authentication.
 */
@RestController
@RequestMapping("/patients")
public class AuthPatientController {

    private final AuthenticatePatient authenticatePatient;

    public AuthPatientController(AuthenticatePatient authenticatePatient) {
        this.authenticatePatient = authenticatePatient;
    }

    /**
     * Authenticates a patient with email/phone and password.
     *
     * @param loginRequest contains identifier and password
     * @return login response with authentication token
     */
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticatePatient.login(loginRequest);
    }

    /**
     * Validates the double authentication code sent to the patient.
     *
     * @param validateDoubleAuthRequest contains the double auth code
     * @return response confirming double authentication success
     */
    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public DoubleAuthenticationUserResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticatePatient.validateDoubleAuthCode(validateDoubleAuthRequest);
    }
}
