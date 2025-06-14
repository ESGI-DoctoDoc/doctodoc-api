package fr.esgi.doctodocapi.use_cases.patient.appointment;

import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.use_cases.patient.mappers.AppointmentDetailedMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAppointmentDetail {
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
