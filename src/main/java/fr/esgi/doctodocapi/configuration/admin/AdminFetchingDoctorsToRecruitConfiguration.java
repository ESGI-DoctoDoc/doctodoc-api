package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import fr.esgi.doctodocapi.use_cases.admin.get_doctor_recruitment.GetDoctorsToRecruit;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor_recruitment.IGetDoctorsToRecruit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminFetchingDoctorsToRecruitConfiguration {

    @Bean
    public IGetDoctorsToRecruit getDoctorsToRecruit(DoctorRecruitmentRepository doctorRecruitmentRepository) {
        return new GetDoctorsToRecruit(doctorRecruitmentRepository);
    }
}
