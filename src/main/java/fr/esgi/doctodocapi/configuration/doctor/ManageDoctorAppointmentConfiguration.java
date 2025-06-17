package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_appointment.GetDoctorAppointments;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetDoctorAppointments;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorAppointmentConfiguration {

    @Bean
    public IGetDoctorAppointments getGetDoctorAppointments(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, AppointmentResponseMapper appointmentResponseMapper) {
        return new GetDoctorAppointments(appointmentRepository, userRepository, doctorRepository, getCurrentUserContext, appointmentResponseMapper);
    }
}
