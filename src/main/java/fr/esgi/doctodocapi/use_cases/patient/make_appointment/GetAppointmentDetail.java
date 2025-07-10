package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IGetAppointmentDetail;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class GetAppointmentDetail implements IGetAppointmentDetail {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailedMapper appointmentDetailedMapper;
    private final GetPatientFromContext getPatientFromContext;

    public GetAppointmentDetail(AppointmentRepository appointmentRepository, AppointmentDetailedMapper appointmentDetailedMapper, GetPatientFromContext getPatientFromContext) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailedMapper = appointmentDetailedMapper;
        this.getPatientFromContext = getPatientFromContext;
    }

    public GetAppointmentDetailedResponse get(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Appointment appointment = this.appointmentRepository.getById(id);

            if (!Objects.equals(appointment.getPatient().getUserId(), patient.getUserId())) {
                throw new AppointmentNotFoundException();
            }

            return this.appointmentDetailedMapper.toDto(appointment);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
