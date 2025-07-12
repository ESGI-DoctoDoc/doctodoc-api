package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.make_appointment.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.*;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakingAppointmentConfiguration {
    @Bean
    public IFlowToMakingAppointment flowToMakingAppointment(MedicalConcernRepository medicalConcernRepository, DoctorRepository doctorRepository, DoctorSubscriptionRepository doctorSubscriptionRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentsAvailabilityService appointmentsAvailabilityService, SlotRepository slotRepository, AbsenceRepository absenceRepository) {
        return new FlowToMakingAppointment(medicalConcernRepository, doctorRepository, doctorSubscriptionRepository, careTrackingRepository, getPatientFromContext, appointmentsAvailabilityService, slotRepository, absenceRepository);
    }

    @Bean
    public IGetAppointments getAppointments(UserRepository userRepository, AppointmentRepository appointmentRepository, GetCurrentUserContext getCurrentUserContext, AppointmentPresentationMapper appointmentPresentationMapper) {
        return new GetAppointments(userRepository, appointmentRepository, getCurrentUserContext, appointmentPresentationMapper);
    }

    @Bean
    public IValidateAppointment validateAppointment(SlotRepository slotRepository, PatientRepository patientRepository, MedicalConcernRepository medicalConcernRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository, MailSender mailSender) {
        return new ValidateAppointment(slotRepository, patientRepository, medicalConcernRepository, appointmentRepository, doctorRepository, careTrackingRepository, getPatientFromContext, notificationRepository, mailSender);
    }

    @Bean
    public ICancelAppointment cancelAppointment(AppointmentRepository appointmentRepository, GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository, PatientRepository patientRepository, MailSender mailSender) {
        return new CancelAppointment(appointmentRepository, getPatientFromContext, notificationRepository, patientRepository, mailSender);
    }

    @Bean
    public IGetAppointmentDetail getAppointmentDetail(AppointmentRepository appointmentRepository, AppointmentDetailedMapper appointmentDetailedMapper, GetPatientFromContext getPatientFromContext) {
        return new GetAppointmentDetail(appointmentRepository, appointmentDetailedMapper, getPatientFromContext);
    }
}
