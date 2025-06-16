package fr.esgi.doctodocapi.presentation.doctor;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.OnboardingProcessResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IOnboardingDoctor;
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
    private final IOnboardingDoctor onboardingDoctorProcess;

    public OnBoardingDoctorController(IOnboardingDoctor onboardingDoctorProcess) {
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
