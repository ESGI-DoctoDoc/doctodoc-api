package fr.esgi.doctodocapi.use_cases.admin.get_appointment;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentDetailsForAdmin;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetAppointmentDetailsForAdmin implements IGetAppointmentDetailsForAdmin {
    private final AppointmentRepository appointmentRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final AppointmentResponseMapper appointmentResponseMapper;

    public GetAppointmentDetailsForAdmin(AppointmentRepository appointmentRepository, CareTrackingRepository careTrackingRepository, AppointmentResponseMapper appointmentResponseMapper) {
        this.appointmentRepository = appointmentRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.appointmentResponseMapper = appointmentResponseMapper;
    }

    public GetAppointmentForAdminResponse execute(UUID appointmentId) {
        try {
            Appointment appointment = this.appointmentRepository.getById(appointmentId);
            CareTracking careTracking = appointment.getCareTrackingId() != null
                    ? careTrackingRepository.getById(appointment.getCareTrackingId())
                    : null;
            return this.appointmentResponseMapper.toAdminResponse(appointment, careTracking);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
