package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence;

import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveRangeAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Use case for saving a ranged absence (with start/end date and optional hours)
 * for the currently authenticated doctor.
 */
public class SaveRangeAbsence implements ISaveRangeAbsence {
    private static final List<String> VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN = Arrays.asList(
            AppointmentStatus.CONFIRMED.getValue(),
            AppointmentStatus.UPCOMING.getValue(),
            AppointmentStatus.WAITING_ROOM.getValue()
    );

    private final AbsenceRepository absenceRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final AbsenceResponseMapper absenceResponseMapper;
    private final DoctorRepository doctorRepository;
    private final AbsenceValidationService absenceValidationService;
    private final AppointmentRepository appointmentRepository;
    private final NotificationPushService notificationPushService;

    public SaveRangeAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService, AppointmentRepository appointmentRepository, NotificationPushService notificationPushService) {
        this.absenceRepository = absenceRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
        this.doctorRepository = doctorRepository;
        this.absenceValidationService = absenceValidationService;
        this.appointmentRepository = appointmentRepository;
        this.notificationPushService = notificationPushService;
    }

    /**
     * Creates and persists a ranged absence, after validating it against existing absences.
     *
     * @param request the absence creation request
     * @return the saved absence as a response DTO
     * @throws ApiException if domain validation fails
     */
    public GetAbsenceResponse execute(SaveRangeAbsenceRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Absence absence = Absence.createWithRange(request.description(), request.start(), request.end(), request.startHour(), request.endHour());

            List<Absence> existing = this.absenceRepository.findAllByDoctorId(doctor.getId());
            this.absenceValidationService.validateNoConflictWithExisting(absence, existing);

            doctor.getCalendar().addAbsence(absence);
            cancelAppointmentsDuringAbsence(absence, doctor);

            Absence saved = this.absenceRepository.save(absence, doctor.getId());
            return this.absenceResponseMapper.toResponse(saved);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private void cancelAppointmentsDuringAbsence(Absence absence, Doctor doctor) {
        List<Appointment> appointments = appointmentRepository.findVisibleAppointmentsByDoctorIdAndDateBetween(
                doctor.getId(),
                absence.getAbsenceRange().getDateRange().getStart(),
                absence.getAbsenceRange().getDateRange().getEnd(),
                VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN,
                0,
                Integer.MAX_VALUE
        );

        for (Appointment appointment : appointments) {
            boolean isDateMatch =
                    !appointment.getDate().isBefore(absence.getAbsenceRange().getDateRange().getStart()) &&
                            !appointment.getDate().isAfter(absence.getAbsenceRange().getDateRange().getEnd());

            boolean isTimeOverlap =
                    HoursRange.isTimesOverlap(absence.getAbsenceRange().getHoursRange(), appointment.getHoursRange());

            if (isDateMatch && isTimeOverlap) {
                appointment.cancel("Le rendez-vous a été annulé car il chevauche une période d'absence du docteur.");
                appointmentRepository.cancel(appointment);
                notifyPatient(appointment);
            }
        }
    }

    private void notifyPatient(Appointment appointment) {
        NotificationMessage message = NotificationMessageType.cancelAppointment(
                appointment.getPatient().getUserId(),
                appointment.getDate(),
                appointment.getHoursRange().getStart(),
                appointment.getCancelExplanation()
        );
        this.notificationPushService.send(message);
    }
}
