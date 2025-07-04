package fr.esgi.doctodocapi.use_cases.doctor.manage_patient;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetDoctorPatients;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetDoctorPatients implements IGetDoctorPatients {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorRepository doctorRepository;

    public GetDoctorPatients(AppointmentRepository appointmentRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorRepository = doctorRepository;
    }

    public List<GetDoctorPatientResponse> execute(int page, int size) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<Patient> patients = this.appointmentRepository.getDistinctPatientsByDoctorId(doctor.getId(), page, size);

            return patients.stream().map(patient -> new GetDoctorPatientResponse(
                    patient.getId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getEmail().getValue(),
                    patient.getPhoneNumber().getValue(),
                    patient.getBirthdate().getValue().toString(),
                    patient.getGender().name(),
                    patient.getCreatedAt().toLocalDate()
            )).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
