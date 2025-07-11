package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_appointment.*;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.GetAppointmentsOnAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IGetAppointmentsOnAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.*;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorAppointmentConfiguration {

    @Bean
    public IGetDoctorAppointments getGetDoctorAppointments(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, AppointmentResponseMapper appointmentResponseMapper, CareTrackingRepository careTrackingRepository) {
        return new GetDoctorAppointments(appointmentRepository, userRepository, doctorRepository, getCurrentUserContext, appointmentResponseMapper, careTrackingRepository);
    }

    @Bean
    public ISaveDoctorAppointment saveDoctorAppointment(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, PatientRepository patientRepository, MedicalConcernRepository medicalConcernRepository, SlotRepository slotRepository) {
        return new SaveDoctorAppointment(doctorRepository, appointmentRepository, medicalConcernRepository, patientRepository, getCurrentUserContext, userRepository, slotRepository);
    }

    @Bean
    public IGetAppointmentsAvailabilityForDoctor getAppointmentsAvailabilityForDoctor(MedicalConcernRepository medicalConcernRepository, AppointmentsAvailabilityService appointmentsAvailabilityService) {
        return new GetAppointmentsAvailabilityForDoctor(medicalConcernRepository, appointmentsAvailabilityService);
    }

    @Bean
    public ICancelDoctorAppointment cancelDoctorAppointment(AppointmentRepository appointmentRepository, GetDoctorFromContext getDoctorFromContext, NotificationPushService notificationPushService) {
        return new CancelDoctorAppointment(appointmentRepository, getDoctorFromContext, notificationPushService);
    }

    @Bean
    public ICompleteAppointment completeAppointment(AppointmentRepository appointmentRepository, GetDoctorFromContext getDoctorFromContext) {
        return new CompleteAppointment(getDoctorFromContext, appointmentRepository);
    }

    @Bean
    public IUpdateDoctorAppointment updateDoctorAppointment(IGetDoctorFromContext getDoctorFromContext, AppointmentRepository appointmentRepository, MedicalConcernRepository medicalConcernRepository, SlotRepository slotRepository) {
        return new UpdateDoctorAppointment(getDoctorFromContext, appointmentRepository, medicalConcernRepository, slotRepository);
    }
}
