package fr.esgi.doctodocapi.use_cases.care_tracking.book_appointment_in_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingNotFoundException;
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
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.book_appointment_in_care_tracking.BookAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests.book_appointment_in_care_tracking.PreAppointmentAnswerRequest;
import fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses.book_appointment_in_care_tracking.BookedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.care_tracking.ports.in.book_appointment_in_care_tracking.IBookAppointmentInCareTracking;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
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

    public BookAppointmentInCareTracking(CareTrackingRepository careTrackingRepository, AppointmentRepository appointmentRepository, SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, PatientRepository patientRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.patientRepository = patientRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }


    public BookedAppointmentResponse execute(BookAppointmentRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = retrieveAndValidateCareTracking(request.careTrackingId());
            Patient patient = retrieveAndValidatePatient(request.patientId(), doctor);

            MedicalConcern medicalConcern = this.medicalConcernRepository.getMedicalConcernById(request.medicalConcernId(), doctor);
            Slot slot = this.slotRepository.findOneByMedicalConcernAndDate(medicalConcern.getId(), request.start());

            List<PreAppointmentAnswers> answers = extractPreAppointmentAnswers(request.answers(), medicalConcern);

            Appointment appointment = Appointment.initFromDoctor(slot, patient, doctor, medicalConcern, request.startHour(), answers, request.notes(), careTracking.getId());
            careTracking.addAppointment(appointment.getId());

            UUID savedAppointment = this.appointmentRepository.save(appointment);
            return new BookedAppointmentResponse(savedAppointment);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private CareTracking retrieveAndValidateCareTracking(UUID careTrackingId) {
        CareTracking careTracking = this.careTrackingRepository.getById(careTrackingId);

        if (careTracking.getClosedAt() != null) {
            throw new ClosedCareTrackingException();
        }

        return careTracking;
    }

    private Patient retrieveAndValidatePatient(UUID patientId, Doctor doctor) {
        if (!this.appointmentRepository.existsPatientByDoctorAndPatientId(doctor.getId(), patientId)) {
            throw new PatientNotFoundException();
        }
        return this.patientRepository.getById(patientId);
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
}
