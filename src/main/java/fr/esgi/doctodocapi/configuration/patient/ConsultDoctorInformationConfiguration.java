package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.SearchDoctorService;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.GetDoctorDetail;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.GetDoctorDetailMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.IGetDoctorDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.search_doctor.ISearchDoctor;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctor;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctorPresentationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsultDoctorInformationConfiguration {

    @Bean
    public IGetDoctorDetail getDoctorDetail(DoctorRepository doctorRepository, GetDoctorDetailMapper getDoctorDetailMapper) {
        return new GetDoctorDetail(doctorRepository, getDoctorDetailMapper);
    }

    @Bean
    public ISearchDoctor getSearchDoctor(SearchDoctorService searchDoctorService, SearchDoctorPresentationMapper searchDoctorPresentationMapper) {
        return new SearchDoctor(searchDoctorService, searchDoctorPresentationMapper);
    }
}
