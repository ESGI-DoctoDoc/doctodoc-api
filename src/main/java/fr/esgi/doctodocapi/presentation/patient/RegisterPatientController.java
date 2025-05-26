package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.use_cases.user.RegisterUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling patient registration.
 */
@RestController
@RequestMapping("/patients")
public class RegisterPatientController {
    private final RegisterUser registerUser;

    public RegisterPatientController(RegisterUser registerUser) {
        this.registerUser = registerUser;
    }

    /**
     * Registers a new patient user.
     *
     * @param registerRequest the registration data containing email, password, phone number, etc.
     * @return a response confirming successful registration
     */
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerPatient(@Valid @RequestBody RegisterRequest registerRequest) {
        return this.registerUser.register(registerRequest);
    }
}
