package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IRegisterUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle doctor registration.
 */
@RestController
@RequestMapping("/doctors")
public class RegisterDoctorController {
    private final IRegisterUser registerUser;

    public RegisterDoctorController(IRegisterUser registerDoctor) {
        this.registerUser = registerDoctor;
    }

    /**
     * Registers a new doctor user.
     *
     * @param registerRequest contains email, password, and phone number
     * @return response confirming registration
     */
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerDoctor(@Valid @RequestBody RegisterRequest registerRequest) {
        return this.registerUser.register(registerRequest);
    }
}
