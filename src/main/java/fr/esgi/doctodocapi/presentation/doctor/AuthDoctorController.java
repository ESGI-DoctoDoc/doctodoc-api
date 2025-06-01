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

/**
 * REST controller to handle authentication requests for doctors.
 */
@RestController
@RequestMapping("/doctors")
public class AuthDoctorController {
    private final AuthenticateDoctor authenticateDoctor;
    private final ResetPassword resetPassword;

    public AuthDoctorController(AuthenticateDoctor authenticateDoctor, ResetPassword resetPassword) {
        this.authenticateDoctor = authenticateDoctor;
        this.resetPassword = resetPassword;
    }

    /**
     * Authenticates a doctor using login credentials.
     *
     * @param loginRequest the login request containing credentials
     * @return a login response containing an authentication token
     */
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticateDoctor.login(loginRequest);
    }

    /**
     * Validates the double authentication code for a doctor.
     *
     * @param validateDoubleAuthRequest the request containing the double authentication code
     * @return a response confirming successful double authentication
     */
    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public DoubleAuthenticationUserResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateDoctor.validateDoubleAuthCode(validateDoubleAuthRequest);
    }

    /**
     * Initiates the password reset process for a doctor.
     *
     * @param resetPasswordRequest the request containing the doctor's email address
     * @return a response indicating whether the reset process was successfully initiated
     */
    @PostMapping("/password-reset")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestResetPasswordResponse requestResetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return this.resetPassword.requestResetPassword(resetPasswordRequest);
    }

    /**
     * Updates the doctor's password using a valid reset token.
     *
     * @param updatePasswordRequest the request containing the new password and reset token
     * @return a response indicating whether the password was successfully updated
     */
    @PutMapping("/password-reset")
    @ResponseStatus(value = HttpStatus.OK)
    public UpdatePasswordResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return this.resetPassword.updatePassword(updatePasswordRequest);
    }
}
