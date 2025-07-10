package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking.UpdateCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.IUpdateCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdateCareTracking implements IUpdateCareTracking {
    private final CareTrackingRepository careTrackingRepository;
    private final GetCurrentUserContext currentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public UpdateCareTracking(CareTrackingRepository careTrackingRepository, GetCurrentUserContext currentUserContext, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.currentUserContext = currentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public GetCareTrackingResponse execute(UUID careTrackingId, UpdateCareTrackingRequest request) {
        String username = currentUserContext.getUsername();
        try {
            User user = userRepository.findByEmail(username);
            Doctor doctor = doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);
            careTracking.update(request.name(), request.description());
            CareTracking savedCareTracking = careTrackingRepository.update(careTracking);
            return new GetCareTrackingResponse(
                    savedCareTracking.getId(),
                    savedCareTracking.getCaseName(),
                    savedCareTracking.getDescription()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
