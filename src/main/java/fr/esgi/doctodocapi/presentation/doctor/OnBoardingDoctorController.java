package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.DoctorValidationRequest;
import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.OnboardingProcessResponse;
import fr.esgi.doctodocapi.domain.use_cases.doctor.manage_doctor_account.OnboardingDoctorProcess;
import fr.esgi.doctodocapi.domain.use_cases.admin.validate_account.ValidateDoctorAccount;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for doctor onboarding process and account validation.
 */
@RestController
@RequestMapping("doctors/onboarding")
public class OnBoardingDoctorController {
    private final OnboardingDoctorProcess onboardingDoctorProcess;
    private final ValidateDoctorAccount validateDoctorAccount;

    public OnBoardingDoctorController(OnboardingDoctorProcess onboardingDoctorProcess, ValidateDoctorAccount validateDoctorAccount) {
        this.onboardingDoctorProcess = onboardingDoctorProcess;
        this.validateDoctorAccount = validateDoctorAccount;
    }

    /**
     * Submits doctor onboarding information.
     *
     * @param onBoardingDoctorRequest the onboarding request data
     * @return the response containing onboarding process result
     */
    @PostMapping
    public OnboardingProcessResponse submit(@Valid @RequestBody OnBoardingDoctorRequest onBoardingDoctorRequest) {
        return this.onboardingDoctorProcess.process(onBoardingDoctorRequest);
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
