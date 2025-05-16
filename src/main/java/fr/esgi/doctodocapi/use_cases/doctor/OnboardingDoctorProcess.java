package fr.esgi.doctodocapi.use_cases.doctor;

import fr.esgi.doctodocapi.dtos.requests.doctor.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.OnboardingProcessResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.exceptions.on_boarding.DoctorAccountAlreadyExist;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OnboardingDoctorProcess {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public OnboardingDoctorProcess(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public OnboardingProcessResponse process(OnBoardingDoctorRequest request) {
        User user = this.getAuthenticatedUserOrThrow();

        try {
            boolean doctorAlreadyExist = this.doctorRepository.isExistsById(user.getId());
            if (doctorAlreadyExist) {
                throw new DoctorAccountAlreadyExist();
            }

            Doctor doctor = Doctor.createFromOnBoarding(user, request);
            doctorRepository.save(doctor);
            return new OnboardingProcessResponse(user.getId());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private User getAuthenticatedUserOrThrow() {
        String email = this.getCurrentUserContext.getUsername();

        try {
            return this.userRepository.findByEmail(email);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}