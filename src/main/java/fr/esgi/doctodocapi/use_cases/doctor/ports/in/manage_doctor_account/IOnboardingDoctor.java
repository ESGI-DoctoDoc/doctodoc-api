package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.OnboardingProcessResponse;

public interface IOnboardingDoctor {
    OnboardingProcessResponse process(OnBoardingDoctorRequest request);
}
