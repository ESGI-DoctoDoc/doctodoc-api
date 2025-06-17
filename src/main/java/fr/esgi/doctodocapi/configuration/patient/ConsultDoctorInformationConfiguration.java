package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.SearchDoctorService;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.GetDoctorDetail;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.GetDoctorDetailMapper;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.OpeningHoursCalculator;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctor;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctorPresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.IGetDoctorDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.search_doctor.ISearchDoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsultDoctorInformationConfiguration {
    @Bean
    public OpeningHoursCalculator getOpeningHours() {
        return new OpeningHoursCalculator();
    }

    @Bean
    public IGetDoctorDetail getDoctorDetail(DoctorRepository doctorRepository, GetDoctorDetailMapper getDoctorDetailMapper, OpeningHoursCalculator openingHoursCalculator) {
        return new GetDoctorDetail(doctorRepository, getDoctorDetailMapper, openingHoursCalculator);
    }

    @Bean
    public ISearchDoctor getSearchDoctor(SearchDoctorService searchDoctorService, SearchDoctorPresentationMapper searchDoctorPresentationMapper) {
        return new SearchDoctor(searchDoctorService, searchDoctorPresentationMapper);
    }
}
