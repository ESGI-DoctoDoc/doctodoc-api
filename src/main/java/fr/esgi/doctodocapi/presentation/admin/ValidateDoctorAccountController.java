package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.DoctorValidationRequest;
import fr.esgi.doctodocapi.use_cases.admin.validate_doctor_account.ValidateDoctorAccount;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for doctor onboarding process and account validation.
 */
@RestController
@RequestMapping("doctors/onboarding")
public class ValidateDoctorAccountController {
    private final ValidateDoctorAccount validateDoctorAccount;

    public ValidateDoctorAccountController(ValidateDoctorAccount validateDoctorAccount) {
        this.validateDoctorAccount = validateDoctorAccount;
    }

    /**
     * Validates a doctor's account.
     * Requires admin role.
     *
     * @param request the doctor validation request containing doctor ID
     */
    @PatchMapping("/validate-account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void validateDoctorAccount(@Valid @RequestBody DoctorValidationRequest request) {
        this.validateDoctorAccount.validateDoctorAccount(request);
    }
}
