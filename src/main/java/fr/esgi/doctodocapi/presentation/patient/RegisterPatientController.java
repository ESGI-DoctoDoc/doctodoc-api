package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.use_cases.user.RegisterUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class RegisterPatientController {
    private final RegisterUser registerUser;


    public RegisterPatientController(RegisterUser registerDoctor) {
        this.registerUser = registerDoctor;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerDoctor(@Valid @RequestBody RegisterRequest registerRequest) {
        this.registerUser.register(registerRequest);
    }
}
