package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.book_appointment_in_care_tracking;

import fr.esgi.doctodocapi.infrastructure.brevo.Invitation;
import fr.esgi.doctodocapi.infrastructure.brevo.Organizer;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.ClosedCareTrackingException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.book_appointment_in_care_tracking.PreAppointmentAnswerRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.book_appointment_in_care_tracking.BookedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.book_appointment_in_care_tracking.IBookAppointmentInCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookAppointmentInCareTracking implements IBookAppointmentInCareTracking {
    private final CareTrackingRepository careTrackingRepository;
    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final PatientRepository patientRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MailSender mailSender;

    public BookAppointmentInCareTracking(CareTrackingRepository careTrackingRepository, AppointmentRepository appointmentRepository, SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, PatientRepository patientRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, MailSender mailSender) {
        this.careTrackingRepository = careTrackingRepository;
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.patientRepository = patientRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.mailSender = mailSender;
    }


    public BookedAppointmentResponse execute(BookAppointmentRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Patient patient = retrieveAndValidatePatient(request.patientId(), doctor);
            CareTracking careTracking = retrieveAndValidateCareTracking(request.careTrackingId(), patient);

            MedicalConcern medicalConcern = this.medicalConcernRepository.getMedicalConcernById(request.medicalConcernId(), doctor);
            Slot slot = this.slotRepository.findOneByMedicalConcernAndDate(medicalConcern.getId(), request.start());

            List<PreAppointmentAnswers> answers = extractPreAppointmentAnswers(request.answers(), medicalConcern);

            Appointment appointment = Appointment.initFromDoctor(slot, patient, doctor, medicalConcern, request.startHour(), answers, request.notes(), careTracking.getId());
            careTracking.addAppointment(appointment.getId());

            UUID savedAppointment = this.appointmentRepository.save(appointment);
            sendMailToPatient(appointment);
            return new BookedAppointmentResponse(savedAppointment);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private Patient retrieveAndValidatePatient(UUID patientId, Doctor doctor) {
        if (!this.appointmentRepository.existsPatientByDoctorAndPatientId(doctor.getId(), patientId)) {
            throw new PatientNotFoundException();
        }
        return this.patientRepository.getById(patientId);
    }

    private CareTracking retrieveAndValidateCareTracking(UUID careTrackingId, Patient patient) {
        CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);

        if (careTracking.getClosedAt() != null) {
            throw new ClosedCareTrackingException();
        }

        return careTracking;
    }

    private List<PreAppointmentAnswers> extractPreAppointmentAnswers(List<PreAppointmentAnswerRequest> responses, MedicalConcern medicalConcern) {
        List<PreAppointmentAnswers> answers = new ArrayList<>();
        List<UUID> validQuestionIds = medicalConcernRepository.getDoctorQuestions(medicalConcern)
                .stream()
                .map(Question::getId)
                .toList();

        for (PreAppointmentAnswerRequest response : responses) {
            Question question = this.medicalConcernRepository.getQuestionById(response.questionId());
            if (question == null || !validQuestionIds.contains(question.getId())) {
                throw new QuestionNotFoundException();
            }
            answers.add(PreAppointmentAnswers.of(question, response.answer()));
        }
        return answers;
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

        String subject = "Confirmation de votre rendez-vous médical avec le Dr " + doctorFirstName + " " + doctorLastName;

        String body = String.format("""
                        Bonjour %s,
                        
                        Votre rendez-vous avec le Dr %s %s a bien été confirmé.
                        
                        📅 Date : %s
                        🕒 Heure : %s
                        📍 Lieu : %s
                        
                        Merci de vous présenter 10 minutes à l’avance muni(e) de votre carte vitale et d’une pièce d’identité.
                        
                        En cas d’empêchement, vous pouvez annuler ou reprogrammer votre rendez-vous via votre espace personnel.
                        
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
                body,
                new Invitation(
                        new Organizer(
                                appointment.getDoctor().getEmail().getValue(),
                                doctorFirstName,
                                doctorLastName
                        ),
                        clinicAddress,
                        LocalDateTime.of(appointment.getDate(), appointment.getHoursRange().getStart()),
                        LocalDateTime.of(appointment.getDate(), appointment.getHoursRange().getEnd())
                )
        );
    }
}
