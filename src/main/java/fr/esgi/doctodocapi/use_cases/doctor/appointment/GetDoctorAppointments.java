package fr.esgi.doctodocapi.use_cases.doctor.appointment;

import fr.esgi.doctodocapi.dtos.responses.doctor.appointment.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDoctorAppointments {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final AppointmentResponseMapper appointmentResponseMapper;

    public GetDoctorAppointments(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, AppointmentResponseMapper appointmentResponseMapper) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.appointmentResponseMapper = appointmentResponseMapper;
    }


    public List<GetDoctorAppointmentResponse> execute(int page, int size) {
        try {
            String email = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(email);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<Appointment> appointments = this.appointmentRepository.getAllByDoctor(doctor.getId(), page, size);
            return appointments.stream()
                    .map(appointmentResponseMapper::toResponse)
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
