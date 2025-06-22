package fr.esgi.doctodocapi.use_cases.care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.doctor_managing_care_tracking.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.doctor_managing_care_tracking.InitializeCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.doctor_managing_care_tracking.IInitializeCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InitializeCareTracking implements IInitializeCareTracking {
    private final CareTrackingRepository careTrackingRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final PatientRepository patientRepository;

    public InitializeCareTracking(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, PatientRepository patientRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.patientRepository = patientRepository;
    }

    public InitializeCareTrackingResponse execute(SaveCareTrackingRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Patient patient = this.patientRepository.getById(request.patientId());

            CareTracking careTracking = CareTracking.initialize(
                        request.name(),
                        request.description(),
                        doctor.getId(),
                        patient
            );

            UUID savedCareTracking = this.careTrackingRepository.save(careTracking);
            return new InitializeCareTrackingResponse(savedCareTracking);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
