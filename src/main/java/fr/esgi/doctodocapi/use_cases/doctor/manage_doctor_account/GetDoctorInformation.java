package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.DoctorInfo;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.DoctorInfoResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.UserInfo;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information.IGetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class GetDoctorInformation implements IGetDoctorInformation {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;

    public GetDoctorInformation(UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
    }

    public DoctorInfoResponse execute() {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            String role = this.getCurrentUserContext.getRole();

            Optional<Doctor> optionalDoctor = this.doctorRepository.getByUserId(user.getId());

            boolean hasOnBoardingDone = false;
            boolean hasLicense = false;
            boolean isVerified = false;

            UUID doctorId = null;
            String firstName = null;
            String lastName = null;

            if (optionalDoctor.isPresent()) {
                Doctor doctor = optionalDoctor.get();
                doctorId = doctor.getId();
                firstName = doctor.getPersonalInformations().getFirstName();
                lastName = doctor.getPersonalInformations().getLastName();
                isVerified = doctor.isVerified();
                hasOnBoardingDone = true;

                hasLicense = this.doctorSubscriptionRepository
                        .findActivePaidSubscriptionByDoctorId(doctor.getId())
                        .isPresent();
            }

            UserInfo userInfo = new UserInfo(
                    user.getId(),
                    user.getEmail().getValue(),
                    user.getPhoneNumber().getValue(),
                    role.toLowerCase(),
                    user.isEmailVerified(),
                    user.isDoubleAuthActive()
            );

            DoctorInfo doctorInfo = new DoctorInfo(
                    doctorId,
                    hasOnBoardingDone,
                    firstName,
                    lastName,
                    hasLicense,
                    isVerified
            );

            return new DoctorInfoResponse(
                    userInfo,
                    doctorInfo
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
