package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking.SaveCareTrackingRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.doctor_managing_care_tracking.InitializeCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.doctor_managing_care_tracking.doctor_managing_care_tracking.IInitializeCareTracking;
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
    private final AppointmentRepository appointmentRepository;


    public InitializeCareTracking(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public InitializeCareTrackingResponse execute(SaveCareTrackingRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Patient patient = retrieveAndValidatePatient(request.patientId(), doctor);

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

    private Patient retrieveAndValidatePatient(UUID patientId, Doctor doctor) {
        if (!this.appointmentRepository.existsPatientByDoctorAndPatientId(doctor.getId(), patientId)) {
            throw new PatientNotFoundException();
        }
        return this.patientRepository.getById(patientId);
    }
}
