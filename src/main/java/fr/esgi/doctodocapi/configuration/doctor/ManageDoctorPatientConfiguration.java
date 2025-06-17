package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_patient.GetDoctorPatients;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetDoctorPatients;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorPatientConfiguration {

    @Bean
    public IGetDoctorPatients getDoctorPatients(AppointmentRepository appointmentRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository) {
        return new GetDoctorPatients(appointmentRepository, userRepository, getCurrentUserContext, doctorRepository);
    }
}
