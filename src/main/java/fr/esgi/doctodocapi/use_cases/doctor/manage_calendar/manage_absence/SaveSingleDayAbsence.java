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
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveSingleDayAbsence;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Use case for saving a single-day absence for the currently authenticated doctor.
 */
public class SaveSingleDayAbsence implements ISaveSingleDayAbsence {
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
    private final MailSender mailSender;
    private final PatientRepository patientRepository;

    public SaveSingleDayAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService, AppointmentRepository appointmentRepository, NotificationPushService notificationPushService, MailSender mailSender, PatientRepository patientRepository) {
        this.absenceRepository = absenceRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
        this.doctorRepository = doctorRepository;
        this.absenceValidationService = absenceValidationService;
        this.appointmentRepository = appointmentRepository;
        this.notificationPushService = notificationPushService;
        this.mailSender = mailSender;
        this.patientRepository = patientRepository;
    }

    /**
     * Creates and persists a single-day absence, after validating it against existing absences.
     *
     * @param request the absence creation request
     * @return the saved absence as a response DTO
     * @throws ApiException if domain validation fails
     */
    public GetAbsenceResponse execute(SaveSingleDayAbsenceRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Absence absence = Absence.createSingleDate(request.description(), request.date());
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
            if (AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
                continue;
            }

            boolean isDateMatch =
                    !appointment.getDate().isBefore(absence.getAbsenceRange().getDateRange().getStart()) &&
                            !appointment.getDate().isAfter(absence.getAbsenceRange().getDateRange().getEnd());

            boolean isTimeOverlap =
                    HoursRange.isTimesOverlap(absence.getAbsenceRange().getHoursRange(), appointment.getHoursRange());

            if (isDateMatch && isTimeOverlap) {
                appointment.cancel("Le rendez-vous a été annulé car il chevauche une période d'absence du docteur.");
                appointmentRepository.cancel(appointment);
                sendMailToPatient(appointment);
                notifyPatient(appointment);
            }
        }
    }

    /// Gestion des notifications et mail (à déplacer)

    private void sendMailToPatient(Appointment appointment) {
        Patient appointmentPatient = appointment.getPatient();
        sendMail(appointmentPatient, appointment);

        if (!appointmentPatient.isMainAccount()) {
            Patient mainPatient = this.patientRepository.getByUserId(appointmentPatient.getUserId()).orElseThrow(PatientNotFoundException::new);
            if (!Objects.equals(mainPatient.getEmail(), appointmentPatient.getEmail())) {
                sendMail(mainPatient, appointment);
            }
        }
    }

    private void sendMail(Patient patient, Appointment appointment) {
        String doctorFirstName = appointment.getDoctor().getPersonalInformations().getFirstName();
        String doctorLastName = appointment.getDoctor().getPersonalInformations().getLastName();
        String patientFirstName = patient.getFirstName();
        String appointmentDate = appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String appointmentTime = appointment.getHoursRange().getStart().format(DateTimeFormatter.ofPattern("HH:mm"));
        String clinicAddress = appointment.getDoctor().getConsultationInformations().getAddress();

        String subject = "Annulation du rendez-vous médical avec le Dr " + doctorFirstName + " " + doctorLastName;

        String body = String.format("""
                        Bonjour %s,
                        
                        Votre rendez-vous avec le Dr %s %s a été annulé.
                        
                        📅 Date : %s
                        🕒 Heure : %s
                        📍 Lieu : %s
                        
                        Cordialement,
                        Doctodoc.
                        """,
                patientFirstName,
                doctorFirstName,
                doctorLastName,
                appointmentDate,
                appointmentTime,
                clinicAddress
        );

        this.mailSender.sendMail(
                patient.getEmail().getValue(),
                subject,
                body
        );
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
