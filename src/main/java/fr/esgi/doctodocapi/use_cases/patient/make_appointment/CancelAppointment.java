package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.CancelAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class CancelAppointment implements ICancelAppointment {
    private final AppointmentRepository appointmentRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final NotificationRepository notificationRepository;
    private final PatientRepository patientRepository;
    private final MailSender mailSender;

    public CancelAppointment(AppointmentRepository appointmentRepository, GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository, PatientRepository patientRepository, MailSender mailSender) {
        this.appointmentRepository = appointmentRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.notificationRepository = notificationRepository;
        this.patientRepository = patientRepository;
        this.mailSender = mailSender;
    }

    public void cancel(UUID id, CancelAppointmentRequest cancelAppointmentRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Appointment appointment = this.appointmentRepository.getById(id);

            if (!Objects.equals(appointment.getPatient().getUserId(), patient.getUserId())) {
                throw new AppointmentNotFoundException();
            }

            appointment.cancel(cancelAppointmentRequest.reason());
            this.appointmentRepository.cancel(appointment);
            sendMailToPatient(appointment);
            notifyDoctorOfCancelAppointment(patient, appointment);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /// Gestion des notifications et rendez-vous (à déplacer)

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
                        
                        Votre rendez-vous avec le Dr %s %s a été annulé.
                        
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


    private void notifyDoctorOfCancelAppointment(Patient patient, Appointment appointment) {
        String patientFullName = patient.getFirstName() + " " + patient.getLastName();
        Notification notification = NotificationsType.cancelAppointment(
                appointment.getDoctor().getId(),
                appointment.getDate(),
                appointment.getHoursRange().getStart(),
                patientFullName
        );
        this.notificationRepository.save(notification);
    }
}
