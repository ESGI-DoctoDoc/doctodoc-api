package fr.esgi.doctodocapi.use_cases.care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.CareTrackingPatientInfo;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.GetCareTrackingsResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.IGetCareTrackings;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetCareTrackings implements IGetCareTrackings {
    private final CareTrackingRepository careTrackingRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public GetCareTrackings(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public List<GetCareTrackingsResponse> execute(int page, int size) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            return this.careTrackingRepository.findAll(doctor.getId(), page, size)
                    .stream()
                    .map(careTracking -> new GetCareTrackingsResponse(
                            careTracking.getId(),
                            careTracking.getCaseName(),
                            careTracking.getCreatedAt().toString(),
                            new CareTrackingPatientInfo(
                                    careTracking.getPatient().getId(),
                                    careTracking.getPatient().getFirstName(),
                                    careTracking.getPatient().getLastName(),
                                    careTracking.getPatient().getEmail().getValue(),
                                    careTracking.getPatient().getPhoneNumber().getValue()
                            )
                    ))
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
