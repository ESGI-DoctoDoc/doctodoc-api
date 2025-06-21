package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.DoctorInfoResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.UserInfo;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information.IGetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class GetDoctorInformation implements IGetDoctorInformation {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public GetDoctorInformation(UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public DoctorInfoResponse execute() {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Optional<Doctor> optionalDoctor = this.doctorRepository.getByUserId(user.getId());
            boolean hasOnBoardingDone = optionalDoctor.isPresent();
            boolean hasLicense = false; // todo à modifier après avoir setup stripe
            boolean isVerified = optionalDoctor.map(Doctor::isVerified).orElse(false);

            UserInfo userInfo = new UserInfo(
                    user.getEmail().getValue(),
                    user.getPhoneNumber().getValue(),
                    user.isEmailVerified(),
                    user.isDoubleAuthActive(),
                    user.getDoubleAuthCode(),
                    user.getCreatedAt()
            );

            return new DoctorInfoResponse(
                    userInfo,
                    hasOnBoardingDone,
                    hasLicense,
                    isVerified
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
