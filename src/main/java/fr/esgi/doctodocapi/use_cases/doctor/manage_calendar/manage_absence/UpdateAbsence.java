package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence;

import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.ExpiredAbsenceException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.UpdateRangeAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.UpdateSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IUpdateAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UpdateAbsence implements IUpdateAbsence {

    private static final List<String> VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN = Arrays.asList(
            AppointmentStatus.CONFIRMED.getValue(),
            AppointmentStatus.UPCOMING.getValue(),
            AppointmentStatus.WAITING_ROOM.getValue()
    );

    private final AbsenceRepository absenceRepository;
    private final AppointmentRepository appointmentRepository;
    private final AbsenceResponseMapper absenceResponseMapper;
    private final AbsenceValidationService absenceValidationService;
    private final IGetDoctorFromContext getDoctorFromContext;
    private final NotificationPushService notificationPushService;
    private final MailSender mailSender;
    private final PatientRepository patientRepository;

    public UpdateAbsence(AbsenceRepository absenceRepository, AppointmentRepository appointmentRepository, AbsenceResponseMapper absenceResponseMapper, AbsenceValidationService absenceValidationService, IGetDoctorFromContext getDoctorFromContext, NotificationPushService notificationPushService, MailSender mailSender, PatientRepository patientRepository) {
        this.absenceRepository = absenceRepository;
        this.appointmentRepository = appointmentRepository;
        this.absenceResponseMapper = absenceResponseMapper;
        this.absenceValidationService = absenceValidationService;
        this.getDoctorFromContext = getDoctorFromContext;
        this.notificationPushService = notificationPushService;
        this.mailSender = mailSender;
        this.patientRepository = patientRepository;
    }

    public GetAbsenceResponse updateSingleDay(UUID absenceId, UpdateSingleDayAbsenceRequest request) {
        try {
            Doctor doctor = getDoctorFromContext.get();
            Absence absence = absenceRepository.findById(absenceId);

            absence.updateSingleDay(request.description(), request.date());

            return handleUpdate(absence, doctor);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public GetAbsenceResponse updateRange(UUID absenceId, UpdateRangeAbsenceRequest request) {
        try {
            Doctor doctor = getDoctorFromContext.get();
            Absence absence = absenceRepository.findById(absenceId);

            absence.updateRange(
                    request.description(),
                    request.start(),
                    request.end(),
                    request.startHour(),
                    request.endHour()
            );

            return handleUpdate(absence, doctor);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private GetAbsenceResponse handleUpdate(Absence absence, Doctor doctor) {
        if (absence.getAbsenceRange().getDateRange().getEnd().isBefore(LocalDate.now())) {
            throw new ExpiredAbsenceException();
        }

        List<Absence> existingAbsences = absenceRepository.findAllByDoctorId(doctor.getId())
                .stream()
                .filter(a -> !a.getId().equals(absence.getId()))
                .toList();
        absenceValidationService.validateNoConflictWithExisting(absence, existingAbsences);

        doctor.getCalendar().updateAbsence(absence);
        cancelAppointmentsDuringAbsence(absence, doctor);

        Absence saved = absenceRepository.update(absence);
        return absenceResponseMapper.toResponse(saved);
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
                notifyPatient(appointment);
                sendMailToPatient(appointment);
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