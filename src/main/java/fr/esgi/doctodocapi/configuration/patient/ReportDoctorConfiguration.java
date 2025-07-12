package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.admin.AdminRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.report_doctor.IReportDoctor;
import fr.esgi.doctodocapi.use_cases.patient.report_doctor.ReportDoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportDoctorConfiguration {

    @Bean
    public IReportDoctor reportDoctor(DoctorReportRepository doctorReportRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext, MailSender mailSender, AdminRepository adminRepository, NotificationRepository notificationRepository) {
        return new ReportDoctor(doctorReportRepository, doctorRepository, getPatientFromContext, mailSender, adminRepository, notificationRepository);
    }

}
