package fr.esgi.doctodocapi.use_cases.doctor.manage_patient;

import fr.esgi.doctodocapi.infrastructure.mappers.PatientDetailResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientDetailResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetPatientDetails;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetPatientDetails implements IGetPatientDetails {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final PatientDetailResponseMapper patientDetailResponseMapper;

    public GetPatientDetails(PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, PatientDetailResponseMapper patientDetailResponseMapper) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.patientDetailResponseMapper = patientDetailResponseMapper;
    }

    public GetDoctorPatientDetailResponse execute(UUID patientId) {
        try {
            String email = getCurrentUserContext.getUsername();
            User currentUser = userRepository.findByEmail(email);
            Doctor doctor = doctorRepository.findDoctorByUserId(currentUser.getId());

            if (!appointmentRepository.existsPatientByDoctorAndPatientId(doctor.getId(), patientId)) {
                throw new PatientNotFoundException();
            }

            Patient patient = patientRepository.getById(patientId);
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorIdAndPatientId(
                    doctor.getId(), patientId
            );

            return patientDetailResponseMapper.toResponse(patient, appointments);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
