package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.DoctorValidationRequest;
import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.OnboardingProcessResponse;
import fr.esgi.doctodocapi.use_cases.doctor.OnboardingDoctorProcess;
import fr.esgi.doctodocapi.use_cases.user.ValidateDoctorAccount;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors/onboarding")
public class OnBoardingDoctorController {
    private final OnboardingDoctorProcess onboardingDoctorProcess;
    private final ValidateDoctorAccount validateDoctorAccount;


    public OnBoardingDoctorController(OnboardingDoctorProcess onboardingDoctorProcess, ValidateDoctorAccount validateDoctorAccount) {
        this.onboardingDoctorProcess = onboardingDoctorProcess;
        this.validateDoctorAccount = validateDoctorAccount;
    }

    @PostMapping("/professional-info")
    public OnboardingProcessResponse submit(@Valid @RequestBody OnBoardingDoctorRequest onBoardingDoctorRequest) {
        return this.onboardingDoctorProcess.process(onBoardingDoctorRequest);
    }

    @PatchMapping("/validate-account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void validateDoctorAccount(@Valid @RequestBody DoctorValidationRequest request) {
        this.validateDoctorAccount.validateDoctorAccount(request);
    }
}
