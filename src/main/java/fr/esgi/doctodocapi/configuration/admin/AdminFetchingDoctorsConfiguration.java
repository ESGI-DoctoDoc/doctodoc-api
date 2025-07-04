package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorResponseMapper;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.get_doctor.GetDoctorByIdForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.get_doctor.GetDoctorsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorByIdForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorsForAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminFetchingDoctorsConfiguration {

    @Bean
    public IGetDoctorsForAdmin getDoctorsForAdmin(DoctorRepository doctorRepository, DoctorResponseMapper doctorResponseMapper, SpecialityRepository specialityRepository) {
        return new GetDoctorsForAdmin(doctorRepository, doctorResponseMapper, specialityRepository);
    }

    @Bean
    public IGetDoctorByIdForAdmin getDoctorByIdForAdmin(DoctorRepository doctorRepository, SpecialityRepository specialityRepository, DoctorSubscriptionRepository subscriptionRepository, AppointmentRepository appointmentRepository, DoctorResponseMapper doctorResponseMapper) {
        return new GetDoctorByIdForAdmin(doctorRepository, specialityRepository, subscriptionRepository, appointmentRepository, doctorResponseMapper);
    }
}
