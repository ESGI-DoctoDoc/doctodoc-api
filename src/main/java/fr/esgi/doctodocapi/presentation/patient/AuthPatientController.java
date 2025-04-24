package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.user.AuthenticateUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class AuthPatientController {

    private final AuthenticateUser authenticateUser;

    public AuthPatientController(AuthenticateUser authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, "PATIENT");
    }

    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);
    }

    @GetMapping("hey")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @ResponseStatus(value = HttpStatus.OK)
    public String hey() {
        return "hey";
    }


}
