package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking.GetPatientCareTrackings;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagePatientCareTrackings {
    @Bean
    public IGetPatientCareTrackings patientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext) {
        return new GetPatientCareTrackings(careTrackingRepository, getPatientFromContext);
    }
}
