package fr.esgi.doctodocapi.use_cases.admin.get_appointment;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_appointment.IGetAppointmentsForAdmin;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetAppointmentsForAdmin implements IGetAppointmentsForAdmin {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentResponseMapper appointmentResponseMapper;
    private final CareTrackingRepository careTrackingRepository;

    public GetAppointmentsForAdmin(AppointmentRepository appointmentRepository, AppointmentResponseMapper appointmentResponseMapper, CareTrackingRepository careTrackingRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentResponseMapper = appointmentResponseMapper;
        this.careTrackingRepository = careTrackingRepository;
    }

    public List<GetAppointmentForAdminResponse> getDoctorAppointment(int page, int size) {
        try {
            List<Appointment> appointments = this.appointmentRepository.findAllWithPaginationForAdmin(page, size);

            return appointments.stream()
                    .map(appointment -> {
                        CareTracking careTracking = appointment.getCareTrackingId() != null
                                ? careTrackingRepository.getById(appointment.getCareTrackingId())
                                : null;
                        return appointmentResponseMapper.toAdminResponse(appointment, careTracking);
                    })
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
