package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.use_cases.admin.manage_specialities.AddSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.manage_specialities.GetAllSpecialities;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IAddSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IGetAllSpecialities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageSpecialitiesConfiguration {

    @Bean
    public IAddSpeciality addSpeciality(SpecialityRepository specialityRepository) {
        return new AddSpeciality(specialityRepository);
    }

    @Bean
    public IGetAllSpecialities getGetAllSpecialities(SpecialityRepository specialityRepository) {
        return new GetAllSpecialities(specialityRepository);
    }
}
