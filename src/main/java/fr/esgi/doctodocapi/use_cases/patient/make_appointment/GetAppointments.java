package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment.GetAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IGetAppointments;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;


public class GetAppointments implements IGetAppointments {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final AppointmentPresentationMapper appointmentPresentationMapper;

    public GetAppointments(UserRepository userRepository, AppointmentRepository appointmentRepository, GetCurrentUserContext getCurrentUserContext, AppointmentPresentationMapper appointmentPresentationMapper) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.appointmentPresentationMapper = appointmentPresentationMapper;
    }

    public List<GetAppointmentResponse> getAllPastAppointments(int page, int size) {
        try {

            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            List<Appointment> appointments = this.appointmentRepository.getAllByUserAndStatusOrderByDateDesc(user, AppointmentStatus.COMPLETED, page, size);
            return appointments.stream().map(appointmentPresentationMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    public List<GetAppointmentResponse> getAllUpcomingAppointments(int page, int size) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            List<Appointment> appointments = this.appointmentRepository.getAllByUserAndStatusOrderByDateAsc(user, AppointmentStatus.CONFIRMED, page, size);
            return appointments.stream().map(appointmentPresentationMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    public GetAppointmentResponse getMostRecentUpcomingAppointment() {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Optional<Appointment> appointment = this.appointmentRepository.getMostRecentUpcomingAppointment(user);
            if (appointment.isPresent()) {
                return this.appointmentPresentationMapper.toDto(appointment.get());
            } else {
                throw new ApiException(HttpStatus.NOT_FOUND, "appointment.empty", "not exist upcomings appointments");
            }

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
