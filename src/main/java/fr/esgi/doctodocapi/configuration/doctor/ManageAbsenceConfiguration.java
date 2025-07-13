package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentForAbsenceMapper;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.*;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.GetAppointmentsOnAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.*;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageAbsenceConfiguration {

    @Bean
    public IDeleteAbsence deleteAbsence(AbsenceRepository absenceRepository) {
        return new DeleteAbsence(absenceRepository);
    }

    @Bean
    public IGetAbsences getAbsences(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository) {
        return new GetAbsences(getCurrentUserContext, absenceRepository, userRepository, absenceResponseMapper, doctorRepository);
    }

    @Bean
    public ISaveRangeAbsence saveRangeAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService, AppointmentRepository appointmentRepository, NotificationPushService notificationPushService, MailSender mailSender, PatientRepository patientRepository) {
        return new SaveRangeAbsence(absenceRepository, getCurrentUserContext, userRepository, absenceResponseMapper, doctorRepository, absenceValidationService, appointmentRepository, notificationPushService, mailSender, patientRepository);
    }

    @Bean
    public ISaveSingleDayAbsence saveSingleDayAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService, AppointmentRepository appointmentRepository, NotificationPushService notificationPushService, MailSender mailSender, PatientRepository patientRepository) {
        return new SaveSingleDayAbsence(absenceRepository, getCurrentUserContext, userRepository, absenceResponseMapper, doctorRepository, absenceValidationService, appointmentRepository, notificationPushService, mailSender, patientRepository);
    }

    @Bean
    public IUpdateAbsence updateAbsence(AbsenceRepository absenceRepository, AppointmentRepository appointmentRepository, AbsenceResponseMapper absenceResponseMapper, AbsenceValidationService absenceValidationService, IGetDoctorFromContext getDoctorFromContext, NotificationPushService notificationPushService, MailSender mailSender, PatientRepository patientRepository) {
        return new UpdateAbsence(absenceRepository, appointmentRepository, absenceResponseMapper, absenceValidationService, getDoctorFromContext, notificationPushService, mailSender, patientRepository);
    }

    @Bean
    public IGetAppointmentsOnAbsence getAppointmentsOnAbsence(AppointmentRepository appointmentRepository, IGetDoctorFromContext getDoctorFromContext, AppointmentForAbsenceMapper appointmentForAbsenceMapper) {
        return new GetAppointmentsOnAbsence(appointmentRepository, getDoctorFromContext, appointmentForAbsenceMapper);
    }
}