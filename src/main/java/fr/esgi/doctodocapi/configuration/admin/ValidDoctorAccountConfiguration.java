package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IValidateDoctorAccount;
import fr.esgi.doctodocapi.use_cases.admin.validate_doctor_account.ValidateDoctorAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidDoctorAccountConfiguration {

    @Bean
    public IValidateDoctorAccount validateDoctorAccount(DoctorRepository doctorRepository) {
        return new ValidateDoctorAccount(doctorRepository);
    }
}
