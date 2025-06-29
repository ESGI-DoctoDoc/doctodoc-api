package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
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
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.SaveDoctorAnswersForAnAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.SaveDoctorAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.SaveDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.ISaveDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaveDoctorAppointment implements ISaveDoctorAppointment {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final PatientRepository patientRepository;
    private final GetCurrentUserContext currentUserContext;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    public SaveDoctorAppointment(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, MedicalConcernRepository medicalConcernRepository, PatientRepository patientRepository, GetCurrentUserContext currentUserContext, UserRepository userRepository, SlotRepository slotRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.patientRepository = patientRepository;
        this.currentUserContext = currentUserContext;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
    }

    public SaveDoctorAppointmentResponse execute(SaveDoctorAppointmentRequest request) {
        try {
            String username = this.currentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Patient patient = retrieveAndValidatePatient(request.patientId(), doctor);
            MedicalConcern medicalConcern = this.medicalConcernRepository.getMedicalConcernById(request.medicalConcernId(), doctor);

            List<PreAppointmentAnswers> answers = extractPreAppointmentAnswers(request.answers(), medicalConcern);
            Slot slot = this.slotRepository.findOneByMedicalConcernAndDate(medicalConcern.getId(), request.start());

            Appointment appointment = Appointment.initFromDoctor(slot, patient, doctor, medicalConcern, request.startHour(), answers, request.notes(), null);

            UUID savedAppointmentId = this.appointmentRepository.save(appointment);

            return new SaveDoctorAppointmentResponse(savedAppointmentId);
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

    private List<PreAppointmentAnswers> extractPreAppointmentAnswers(List<SaveDoctorAnswersForAnAppointmentRequest> responses, MedicalConcern medicalConcern) {
        List<PreAppointmentAnswers> answers = new ArrayList<>();

        List<UUID> validQuestionIds = this.medicalConcernRepository.getDoctorQuestions(medicalConcern)
                .stream()
                .map(Question::getId)
                .toList();

        for (SaveDoctorAnswersForAnAppointmentRequest response : responses) {
            Question question = this.medicalConcernRepository.getQuestionById(response.questionId());

            if (question == null || !validQuestionIds.contains(question.getId())) {
                throw new QuestionNotFoundException();
            }

            answers.add(PreAppointmentAnswers.of(question, response.answer()));
        }

        return answers;
    }
}
