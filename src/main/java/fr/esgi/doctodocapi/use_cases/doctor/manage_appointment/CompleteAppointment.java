package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.appointment.exceptions.CannotCompleteAppointmentException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.CompleteAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ICompleteAppointment;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class CompleteAppointment implements ICompleteAppointment {
    private final GetDoctorFromContext getDoctorFromContext;
    private final AppointmentRepository appointmentRepository;

    public CompleteAppointment(GetDoctorFromContext getDoctorFromContext, AppointmentRepository appointmentRepository) {
        this.getDoctorFromContext = getDoctorFromContext;
        this.appointmentRepository = appointmentRepository;
    }

    public CompleteAppointmentResponse process(UUID id) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();
            Appointment appointment = this.appointmentRepository.getById(id);

            LocalDate now = LocalDate.now();

            if (appointment.getDate().isAfter(now)) {
                throw new CannotCompleteAppointmentException();
            }

            if (!Objects.equals(doctor.getId(), appointment.getDoctor().getId())) {
                throw new AppointmentNotFoundException();
            }
            appointment.completed();
            this.appointmentRepository.complete(appointment);
            return new CompleteAppointmentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
