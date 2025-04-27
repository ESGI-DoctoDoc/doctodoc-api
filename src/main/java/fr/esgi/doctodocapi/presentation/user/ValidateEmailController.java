package fr.esgi.doctodocapi.presentation.user;

import fr.esgi.doctodocapi.use_cases.user.ValidateEmail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class ValidateEmailController {

    private final ValidateEmail validateEmail;

    public ValidateEmailController(ValidateEmail validateEmail) {
        this.validateEmail = validateEmail;
    }

    @GetMapping("validate-email")
    @ResponseStatus(value = HttpStatus.OK)
    public void validateEmail(@RequestParam UUID userId) {
        this.validateEmail.validate(userId);
    }
}
