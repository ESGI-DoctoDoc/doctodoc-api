package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.use_cases.admin.manage_speciality.AddSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.manage_speciality.GetAllSpecialities;
import fr.esgi.doctodocapi.use_cases.admin.manage_speciality.UpdateSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality.IAddSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality.IGetAllSpecialities;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality.IUpdateSpeciality;
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

    @Bean
    public IUpdateSpeciality updateSpeciality(SpecialityRepository specialityRepository) {
        return new UpdateSpeciality(specialityRepository);
    }
}
