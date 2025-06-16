package fr.esgi.doctodocapi.use_cases.patient.appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelAppointment {
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
