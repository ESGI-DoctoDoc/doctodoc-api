package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.doctor.AuthenticateDoctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle authentication requests for doctors.
 */
@RestController
@RequestMapping("/doctors")
public class AuthDoctorController {
    private final AuthenticateDoctor authenticateDoctor;

    public AuthDoctorController(AuthenticateDoctor authenticateDoctor) {
        this.authenticateDoctor = authenticateDoctor;
    }

    /**
     * Authenticates a doctor using login credentials.
     *
     * @param loginRequest the login request containing credentials
     * @return a login response containing an authentication token
     */
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticateDoctor.login(loginRequest);
    }

    /**
     * Validates the double authentication code for a doctor.
     *
     * @param validateDoubleAuthRequest the request containing the double authentication code
     * @return a response confirming successful double authentication
     */
    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public DoubleAuthenticationUserResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateDoctor.validateDoubleAuthCode(validateDoubleAuthRequest);
    }
}
