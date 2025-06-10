package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.OnboardingProcessResponse;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.OnboardingDoctorProcess;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for doctor onboarding process and account validation.
 */
@RestController
@RequestMapping("doctors/onboarding")
public class OnBoardingDoctorController {
    private final OnboardingDoctorProcess onboardingDoctorProcess;

    public OnBoardingDoctorController(OnboardingDoctorProcess onboardingDoctorProcess) {
        this.onboardingDoctorProcess = onboardingDoctorProcess;
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
}
