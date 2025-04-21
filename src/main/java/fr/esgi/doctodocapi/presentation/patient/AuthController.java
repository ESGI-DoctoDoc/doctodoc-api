package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requets.LoginRequest;
import fr.esgi.doctodocapi.use_cases.patient.AuthenticatePatient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class AuthController {
    private final AuthenticatePatient authenticatePatient;

    public AuthController(AuthenticatePatient authenticatePatient) {
        this.authenticatePatient = authenticatePatient;
    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest, @RequestHeader String host) {
        String protocol = request.getScheme();
        String hostComplete = protocol + "://" + host + "/";
        return this.authenticatePatient.login(loginRequest, hostComplete);
    }
}
