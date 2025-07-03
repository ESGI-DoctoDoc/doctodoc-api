package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.make_appointment.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.*;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakingAppointmentConfiguration {
    @Bean
    public IFlowToMakingAppointment flowToMakingAppointment(MedicalConcernRepository medicalConcernRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentsAvailabilityService appointmentsAvailabilityService) {
        return new FlowToMakingAppointment(medicalConcernRepository, doctorRepository, careTrackingRepository, getPatientFromContext, appointmentsAvailabilityService);
    }

    @Bean
    public IGetAppointments getAppointments(UserRepository userRepository, AppointmentRepository appointmentRepository, GetCurrentUserContext getCurrentUserContext, AppointmentPresentationMapper appointmentPresentationMapper) {
        return new GetAppointments(userRepository, appointmentRepository, getCurrentUserContext, appointmentPresentationMapper);
    }

    @Bean
    public IValidateAppointment validateAppointment(SlotRepository slotRepository, PatientRepository patientRepository, MedicalConcernRepository medicalConcernRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext) {
        return new ValidateAppointment(slotRepository, patientRepository, medicalConcernRepository, appointmentRepository, doctorRepository, careTrackingRepository, getPatientFromContext);
    }

    @Bean
    public ICancelAppointment cancelAppointment(AppointmentRepository appointmentRepository, GetPatientFromContext getPatientFromContext) {
        return new CancelAppointment(appointmentRepository, getPatientFromContext);
    }

    @Bean
    public IGetAppointmentDetail getAppointmentDetail(AppointmentRepository appointmentRepository, AppointmentDetailedMapper appointmentDetailedMapper, GetPatientFromContext getPatientFromContext) {
        return new GetAppointmentDetail(appointmentRepository, appointmentDetailedMapper, getPatientFromContext);
    }
}
