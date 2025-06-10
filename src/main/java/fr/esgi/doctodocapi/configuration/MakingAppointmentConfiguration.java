package fr.esgi.doctodocapi.configuration;

import fr.esgi.doctodocapi.domain.entities.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.domain.entities.doctor.DoctorRepository;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.domain.use_cases.patient.make_appointment.FlowToMakingAppointment;
import fr.esgi.doctodocapi.domain.use_cases.patient.ports.in.IFlowToMakingAppointment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakingAppointmentConfiguration {
    @Bean
    public IFlowToMakingAppointment flowToMakingAppointment(MedicalConcernRepository medicalConcernRepository, DoctorRepository doctorRepository, AppointmentsAvailabilityService appointmentsAvailabilityService) {
        return new FlowToMakingAppointment(medicalConcernRepository, doctorRepository, appointmentsAvailabilityService);
    }
}
