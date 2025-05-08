package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.doctor.AuthenticateDoctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class AuthDoctorController {
    private final AuthenticateDoctor authenticateDoctor;

    public AuthDoctorController(AuthenticateDoctor authenticateDoctor) {
        this.authenticateDoctor = authenticateDoctor;
    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticateDoctor.login(loginRequest);
    }

    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public DoubleAuthenticationUserResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateDoctor.validateDoubleAuthCode(validateDoubleAuthRequest);
    }
}
