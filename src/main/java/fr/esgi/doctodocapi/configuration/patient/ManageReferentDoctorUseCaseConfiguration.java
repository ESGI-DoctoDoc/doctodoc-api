package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor.SearchDoctorPresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.manage_referent_doctor.GetReferentDoctor;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.IGetReferentDoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageReferentDoctorUseCaseConfiguration {
    @Bean
    public IGetReferentDoctor getReferentDoctor(GetPatientFromContext getPatientFromContext, SearchDoctorPresentationMapper searchDoctorPresentationMapper, DoctorRepository doctorRepository) {
        return new GetReferentDoctor(getPatientFromContext, searchDoctorPresentationMapper, doctorRepository);
    }

}
