package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requets.LoginViaEmailRequest;
import fr.esgi.doctodocapi.use_cases.patient.AuthenticatePatient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class LoginController {
    private final AuthenticatePatient authenticatePatient;

    public LoginController(AuthenticatePatient authenticatePatient) {
        this.authenticatePatient = authenticatePatient;
    }

    @PostMapping("login-with-email")
    public String loginWithEmail(@Valid @RequestBody LoginViaEmailRequest loginViaEmailRequest) {
        return this.authenticatePatient.loginWithEmail(loginViaEmailRequest);
    }
}
