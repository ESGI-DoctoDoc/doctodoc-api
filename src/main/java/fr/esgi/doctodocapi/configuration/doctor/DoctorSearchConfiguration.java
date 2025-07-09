package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSearchPresentationMapper;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IDoctorSearchFetcher;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.RetrieveDoctorSearchData;
import fr.esgi.doctodocapi.use_cases.doctor.search.DoctorSearchFetcher;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorSearchConfiguration {

    @Bean
    public IDoctorSearchFetcher getDoctorSearchFetcher(RetrieveDoctorSearchData retrieveDoctorSearchData, GetCurrentUserContext currentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, DoctorSearchPresentationMapper doctorSearchPresentationMapper, MedicalConcernRepository medicalConcernRepository) {
        return new DoctorSearchFetcher(retrieveDoctorSearchData, currentUserContext, userRepository, doctorRepository, doctorSearchPresentationMapper, medicalConcernRepository);
    }
}
