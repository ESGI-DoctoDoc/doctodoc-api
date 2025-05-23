package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ResetPasswordRequest;
import fr.esgi.doctodocapi.dtos.requests.UpdatePasswordRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.dtos.responses.RequestResetPasswordResponse;
import fr.esgi.doctodocapi.dtos.responses.UpdatePasswordResponse;
import fr.esgi.doctodocapi.use_cases.doctor.AuthenticateDoctor;
import fr.esgi.doctodocapi.use_cases.user.ResetPassword;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class AuthDoctorController {
    private final AuthenticateDoctor authenticateDoctor;
    private final ResetPassword resetPassword;

    public AuthDoctorController(AuthenticateDoctor authenticateDoctor, ResetPassword resetPassword) {
        this.authenticateDoctor = authenticateDoctor;
        this.resetPassword = resetPassword;
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

    @PostMapping("/reset-password/request")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestResetPasswordResponse requestResetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return this.resetPassword.requestResetPassword(request);
    }

    @PostMapping("/reset-password/update")
    @ResponseStatus(value = HttpStatus.OK)
    public UpdatePasswordResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        return this.resetPassword.updatePassword(request);
    }
}
