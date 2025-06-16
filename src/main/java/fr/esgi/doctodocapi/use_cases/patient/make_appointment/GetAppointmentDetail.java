package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_response.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IGetAppointmentDetail;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetAppointmentDetail implements IGetAppointmentDetail {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailedMapper appointmentDetailedMapper;

    public GetAppointmentDetail(AppointmentRepository appointmentRepository, AppointmentDetailedMapper appointmentDetailedMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailedMapper = appointmentDetailedMapper;
    }

    public GetAppointmentDetailedResponse get(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getById(id);
            return this.appointmentDetailedMapper.toDto(appointment);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
