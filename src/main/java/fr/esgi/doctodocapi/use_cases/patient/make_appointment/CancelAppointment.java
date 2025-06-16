package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CancelAppointment implements ICancelAppointment {
    private final AppointmentRepository appointmentRepository;

    public CancelAppointment(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void cancel(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getById(id);
            appointment.cancel();
            this.appointmentRepository.cancel(appointment);
            // todo : send a mail to confirm
            // todo : send a mail for the doctor
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
