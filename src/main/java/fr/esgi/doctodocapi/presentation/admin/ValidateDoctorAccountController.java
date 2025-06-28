package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.ports.in.IManageValidationDoctorAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for doctor onboarding process and account validation.
 */
@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ValidateDoctorAccountController {
    private final IManageValidationDoctorAccount validateDoctorAccount;

    public ValidateDoctorAccountController(IManageValidationDoctorAccount validateDoctorAccount) {
        this.validateDoctorAccount = validateDoctorAccount;
    }

    /**
     * Validates a doctor's account.
     * Requires admin role.
     *
     * @param id the doctor validation request containing doctor ID
     */
    @PatchMapping("doctors/{id}/validate")
    public void validateDoctorAccount(@PathVariable UUID id) {
        this.validateDoctorAccount.validateDoctorAccount(id);
    }

    @PatchMapping("doctors/{id}/refuse")
    public void refuseDoctorAccount(@PathVariable UUID id) {
        this.validateDoctorAccount.refuseDoctorAccount(id);
    }
}
