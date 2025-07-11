package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.*;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.RequestResetPasswordResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.UpdatePasswordResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IAuthenticatePatient;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IChangePassword;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IResetPassword;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle patient authentication.
 */
@RestController
@RequestMapping("/patients")
public class AuthPatientController {

    private final IAuthenticatePatient authenticatePatient;
    private final IResetPassword resetPassword;
    private final IChangePassword changePassword;

    public AuthPatientController(IAuthenticatePatient authenticatePatient, IResetPassword resetPassword, IChangePassword changePassword) {
        this.authenticatePatient = authenticatePatient;
        this.resetPassword = resetPassword;
        this.changePassword = changePassword;
    }

    /**
     * Authenticates a patient with email/phone and password.
     *
     * @param loginRequest contains identifier and password
     * @return login response with authentication token
     */
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticatePatient.login(loginRequest);
    }

    /**
     * Validates the double authentication code sent to the patient.
     *
     * @param validateDoubleAuthRequest contains the double auth code
     * @return response confirming double authentication success
     */
    @PostMapping("validate-double-auth")
    @ResponseStatus(value = HttpStatus.OK)
    public DoubleAuthenticationUserResponse validateDoubleAuth(@Valid @RequestBody ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticatePatient.validateDoubleAuthCode(validateDoubleAuthRequest);
    }

    /**
     * Initiates the password reset process for a patient.
     *
     * @param resetPasswordRequest the request containing the patient's email address
     * @return a response indicating whether the reset process was successfully initiated
     */
    @PostMapping("/password-reset")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestResetPasswordResponse requestResetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return this.resetPassword.requestResetPassword(resetPasswordRequest);
    }

    /**
     * Updates the patient's password using a valid reset token.
     *
     * @param updatePasswordRequest the request containing the new password and reset token
     * @return a response indicating whether the password was successfully updated
     */
    @PutMapping("/password-reset")
    @ResponseStatus(value = HttpStatus.OK)
    public UpdatePasswordResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return this.resetPassword.updatePassword(updatePasswordRequest);
    }

    @PatchMapping("/change-password")
    @ResponseStatus(value = HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        this.changePassword.process(changePasswordRequest);
    }
}
