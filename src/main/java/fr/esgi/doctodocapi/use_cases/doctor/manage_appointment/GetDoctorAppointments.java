package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IGetDoctorAppointments;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GetDoctorAppointments implements IGetDoctorAppointments {
    private static final List<String> VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN = Arrays.asList(
            AppointmentStatus.CONFIRMED.getValue(),
            AppointmentStatus.UPCOMING.getValue(),
            AppointmentStatus.WAITING_ROOM.getValue()
    );

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final AppointmentResponseMapper appointmentResponseMapper;
    private final CareTrackingRepository careTrackingRepository;

    public GetDoctorAppointments(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, AppointmentResponseMapper appointmentResponseMapper, CareTrackingRepository careTrackingRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.appointmentResponseMapper = appointmentResponseMapper;
        this.careTrackingRepository = careTrackingRepository;
    }

    public List<GetDoctorAppointmentResponse> execute(int page, int size, LocalDate startDate) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<Appointment> appointments;
            if (startDate != null) {
                LocalDate endDate = startDate.plusDays(6);
                appointments = this.appointmentRepository.findVisibleAppointmentsByDoctorIdAndDateBetween(
                        doctor.getId(),
                        startDate,
                        endDate,
                        VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN,
                        page,
                        size
                );
            } else {
                appointments = this.appointmentRepository.findVisibleAppointmentsByDoctorIdAndDateAfter(
                        doctor.getId(),
                        LocalDate.now(),
                        VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN,
                        page,
                        size
                );
            }
            return appointments.stream()
                    .map(appointment -> {
                        CareTracking careTracking = appointment.getCareTrackingId() != null
                                ? careTrackingRepository.getById(appointment.getCareTrackingId())
                                : null;
                        return appointmentResponseMapper.toResponse(appointment, careTracking);
                    })
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public GetDoctorAppointmentResponse getById(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getVisibleById(id, VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN);

            CareTracking careTracking = appointment.getCareTrackingId() != null
                    ? this.careTrackingRepository.getById(appointment.getCareTrackingId())
                    : null;

            return this.appointmentResponseMapper.toResponse(appointment, careTracking);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
