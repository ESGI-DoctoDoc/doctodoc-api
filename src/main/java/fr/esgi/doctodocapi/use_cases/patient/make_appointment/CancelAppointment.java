package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.CancelAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.ICancelAppointment;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CancelAppointment implements ICancelAppointment {
    private final AppointmentRepository appointmentRepository;
    private final GetPatientFromContext getPatientFromContext;

    public CancelAppointment(AppointmentRepository appointmentRepository, GetPatientFromContext getPatientFromContext) {
        this.appointmentRepository = appointmentRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public void cancel(UUID id, CancelAppointmentRequest cancelAppointmentRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Appointment appointment = this.appointmentRepository.getByIdAndPatientId(id, patient.getId());
            appointment.cancel(cancelAppointmentRequest.reason());
            this.appointmentRepository.cancel(appointment);
            // todo : send a mail to confirm
            // todo : send a mail for the doctor
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
