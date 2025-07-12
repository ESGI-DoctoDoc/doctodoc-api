package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCanceledAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ICancelDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class CancelDoctorAppointment implements ICancelDoctorAppointment {
    private final AppointmentRepository appointmentRepository;
    private final GetDoctorFromContext getDoctorFromContext;
    private final NotificationPushService notificationPushService;

    public CancelDoctorAppointment(AppointmentRepository appointmentRepository, GetDoctorFromContext getDoctorFromContext, NotificationPushService notificationPushService) {
        this.appointmentRepository = appointmentRepository;
        this.getDoctorFromContext = getDoctorFromContext;
        this.notificationPushService = notificationPushService;
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
            // todo : send a mail to confirm
            notifyPatient(appointment);
            return new GetCanceledAppointmentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
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
