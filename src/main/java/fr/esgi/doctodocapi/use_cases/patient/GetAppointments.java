package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentDoctorResponse;
import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetAppointments {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public GetAppointments(UserRepository userRepository, AppointmentRepository appointmentRepository, GetCurrentUserContext getCurrentUserContext) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public List<GetAppointmentResponse> getAllPastAppointments(int page, int size) {
        try {

            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            List<Appointment> appointments = this.appointmentRepository.getAllByUserAndStatus(user, AppointmentStatus.COMPLETED, page, size);
            return appointments.stream().map(this::mapToGetAppointmentResponse).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    public List<GetAppointmentResponse> getAllUpcomingAppointments(int page, int size) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            List<Appointment> appointments = this.appointmentRepository.getAllByUserAndStatus(user, AppointmentStatus.CONFIRMED, page, size);
            return appointments.stream().map(this::mapToGetAppointmentResponse).toList();

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
                return this.mapToGetAppointmentResponse(appointment.get());
            } else {
                throw new ApiException(HttpStatus.NOT_FOUND, "appointment.empty", "not exist upcomings appointments");
            }

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }


    private GetAppointmentResponse mapToGetAppointmentResponse(Appointment appointment) {
        DoctorPersonnalInformations doctorPersonnalInformations = appointment.getDoctor().getPersonalInformations();
        GetAppointmentDoctorResponse getDoctorAppointmentResponse = new GetAppointmentDoctorResponse(
                appointment.getDoctor().getId(),
                doctorPersonnalInformations.getFirstName(),
                doctorPersonnalInformations.getLastName(),
                appointment.getDoctor().getProfessionalInformations().getSpeciality(),
                doctorPersonnalInformations.getProfilePictureUrl()
        );

        return new GetAppointmentResponse(
                appointment.getId(),
                appointment.getDate(),
                appointment.getHoursRange().getStart(),
                appointment.getDoctor().getConsultationInformations().getAddress(),
                getDoctorAppointmentResponse
        );

    }
}
