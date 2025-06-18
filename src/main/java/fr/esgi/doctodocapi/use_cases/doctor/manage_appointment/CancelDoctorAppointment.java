package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCanceledAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ICancelDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CancelDoctorAppointment implements ICancelDoctorAppointment {
    private final AppointmentRepository appointmentRepository;

    public CancelDoctorAppointment(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public GetCanceledAppointmentResponse cancel(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getById(id);
            appointment.cancel();
            this.appointmentRepository.cancel(appointment);
            // todo : send a mail to confirm
            // todo : send a mail for the doctor
            return new GetCanceledAppointmentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
