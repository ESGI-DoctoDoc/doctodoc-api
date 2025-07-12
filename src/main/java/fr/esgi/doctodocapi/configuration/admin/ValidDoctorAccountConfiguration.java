package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IManageValidationDoctorAccount;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.ManageDoctorValidationAccount;
import fr.esgi.doctodocapi.use_cases.admin.validate_doctor_account.ManageValidationDoctorAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidDoctorAccountConfiguration {

    @Bean
    public IManageValidationDoctorAccount validateDoctorAccount(DoctorRepository doctorRepository, ManageDoctorValidationAccount manageDoctorValidationAccount, MailSender mailSender) {
        return new ManageValidationDoctorAccount(doctorRepository, manageDoctorValidationAccount, mailSender);
    }
}
