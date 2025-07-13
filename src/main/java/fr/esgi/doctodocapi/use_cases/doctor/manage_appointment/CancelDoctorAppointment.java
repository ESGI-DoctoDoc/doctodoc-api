package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCanceledAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ICancelDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class CancelDoctorAppointment implements ICancelDoctorAppointment {
    private final AppointmentRepository appointmentRepository;
    private final GetDoctorFromContext getDoctorFromContext;
    private final NotificationPushService notificationPushService;
    private final PatientRepository patientRepository;
    private final MailSender mailSender;

    public CancelDoctorAppointment(AppointmentRepository appointmentRepository, GetDoctorFromContext getDoctorFromContext, NotificationPushService notificationPushService, PatientRepository patientRepository, MailSender mailSender) {
        this.appointmentRepository = appointmentRepository;
        this.getDoctorFromContext = getDoctorFromContext;
        this.notificationPushService = notificationPushService;
        this.patientRepository = patientRepository;
        this.mailSender = mailSender;
    }

    public GetCanceledAppointmentResponse cancel(UUID id, String reason) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();
            Appointment appointment = this.appointmentRepository.getById(id);
            if (!Objects.equals(doctor.getId(), appointment.getDoctor().getId())) {
                throw new AppointmentNotFoundException();
            }
            appointment.cancel(reason);
            this.appointmentRepository.cancel(appointment);
            notifyPatient(appointment);
            sendMailToPatient(appointment);
            return new GetCanceledAppointmentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /// Gestion des notifications et mail (à déplacer)

    private void sendMailToPatient(Appointment appointment) {
        Patient appointmentPatient = appointment.getPatient();
        sendMail(appointmentPatient, appointment);

        if (!appointmentPatient.isMainAccount()) {
            Patient mainPatient = this.patientRepository.getByUserId(appointmentPatient.getUserId()).orElseThrow(PatientNotFoundException::new);
            if (!Objects.equals(mainPatient.getEmail(), appointmentPatient.getEmail())) {
                sendMail(mainPatient, appointment);
            }
        }
    }

    private void sendMail(Patient patient, Appointment appointment) {
        String doctorFirstName = appointment.getDoctor().getPersonalInformations().getFirstName();
        String doctorLastName = appointment.getDoctor().getPersonalInformations().getLastName();
        String patientFirstName = patient.getFirstName();
        String appointmentDate = appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String appointmentTime = appointment.getHoursRange().getStart().format(DateTimeFormatter.ofPattern("HH:mm"));
        String clinicAddress = appointment.getDoctor().getConsultationInformations().getAddress();

        String subject = "Annulation du rendez-vous médical avec le Dr " + doctorFirstName + " " + doctorLastName;

        String body = String.format("""
                        Bonjour %s,
                        
                        Le Dr %s %s a annulé votre rendez-vous.
                        
                        📅 Date : %s
                        🕒 Heure : %s
                        📍 Lieu : %s
                        
                        Cordialement,
                        Doctodoc.
                        """,
                patientFirstName,
                doctorFirstName,
                doctorLastName,
                appointmentDate,
                appointmentTime,
                clinicAddress
        );

        this.mailSender.sendMail(
                patient.getEmail().getValue(),
                subject,
                body
        );
    }

    private void notifyPatient(Appointment appointment) {
        NotificationMessage message = NotificationMessageType.cancelAppointment(
                appointment.getPatient().getUserId(),
                appointment.getDate(),
                appointment.getHoursRange().getStart(),
                appointment.getCancelExplanation()
        );
        this.notificationPushService.send(message);
    }
}
