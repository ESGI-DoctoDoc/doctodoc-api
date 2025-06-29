package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking.GetPatientCareTrackingDetailed;
import fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking.GetPatientCareTrackingDetailedMapper;
import fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking.GetPatientCareTrackings;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDetailed;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagePatientCareTrackingConfiguration {
    @Bean
    public IGetPatientCareTrackings patientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentRepository appointmentRepository) {
        return new GetPatientCareTrackings(careTrackingRepository, getPatientFromContext, appointmentRepository);
    }

    @Bean
    public IGetPatientCareTrackingDetailed patientCareTrackingDetailed(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, GetPatientCareTrackingDetailedMapper getPatientCareTrackingDetailedMapper, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        return new GetPatientCareTrackingDetailed(careTrackingRepository, getPatientFromContext, getPatientCareTrackingDetailedMapper, doctorRepository, appointmentRepository);
    }
}
