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
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.CancelAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class CancelAppointment implements ICancelAppointment {
    private final AppointmentRepository appointmentRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final NotificationRepository notificationRepository;

    public CancelAppointment(AppointmentRepository appointmentRepository, GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository) {
        this.appointmentRepository = appointmentRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.notificationRepository = notificationRepository;
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
            // todo : send a mail to confirm
            // todo : send a mail for the doctor

            notifyDoctorOfCancelAppointment(patient, appointment);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
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
