package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.use_cases.admin.get_appointment.GetAppointmentDetailsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.get_appointment.GetAppointmentsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentDetailsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentsForAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminFetchingAppointmentsConfiguration {

    @Bean
    public IGetAppointmentsForAdmin getAppointmentsForAdmin(AppointmentRepository appointmentRepository, AppointmentResponseMapper appointmentResponseMapper, CareTrackingRepository careTrackingRepository) {
        return new GetAppointmentsForAdmin(appointmentRepository, appointmentResponseMapper, careTrackingRepository);
    }

    @Bean
    public IGetAppointmentDetailsForAdmin getAppointmentDetailsForAdmin(AppointmentRepository appointmentRepository, CareTrackingRepository careTrackingRepository, AppointmentResponseMapper appointmentResponseMapper) {
        return new GetAppointmentDetailsForAdmin(appointmentRepository, careTrackingRepository, appointmentResponseMapper);
    }
}
