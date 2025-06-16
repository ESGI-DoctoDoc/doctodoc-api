package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.OnboardingProcessResponse;

public interface IOnboardingDoctor {
    OnboardingProcessResponse process(OnBoardingDoctorRequest request);
}
