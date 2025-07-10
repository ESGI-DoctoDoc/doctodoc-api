package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.CloseCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.ICloseCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CloseCareTracking implements ICloseCareTracking {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final CareTrackingRepository careTrackingRepository;

    public CloseCareTracking(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
    }

    public CloseCareTrackingResponse execute(UUID careTrackingId) {
        String username = this.getCurrentUserContext.getUsername();
        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);
            careTracking.close();

            UUID closedCareTracking = this.careTrackingRepository.close(careTracking);
            return new CloseCareTrackingResponse(closedCareTracking);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
