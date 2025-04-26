package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.use_cases.user.RegisterUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class RegisterDoctorController {
    private final RegisterUser registerUser;


    public RegisterDoctorController(RegisterUser registerDoctor) {
        this.registerUser = registerDoctor;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerDoctor(@Valid @RequestBody RegisterRequest registerRequest) {
        return this.registerUser.register(registerRequest);
    }
}
