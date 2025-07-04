package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.infrastructure.mappers.GetDoctorProfileResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetDoctorProfileInformationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IGetDoctorProfileInformation;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

public class GetDoctorProfileInformation implements IGetDoctorProfileInformation {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;
    private final GetDoctorProfileResponseMapper doctorProfileResponseMapper;

    public GetDoctorProfileInformation(UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, GetDoctorProfileResponseMapper doctorProfileResponseMapper) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
        this.doctorProfileResponseMapper = doctorProfileResponseMapper;
    }

    public GetDoctorProfileInformationResponse execute() {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            DoctorSubscription subscription = this.doctorSubscriptionRepository
                    .findLatestSubscriptionByDoctorId(doctor.getId())
                    .orElse(null);

            return this.doctorProfileResponseMapper.toResponse(doctor, subscription);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
