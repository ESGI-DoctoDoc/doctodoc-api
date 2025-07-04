package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.ClosedCareTrackingException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.save_appointment.SaveAnswersForAnAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.save_appointment.SaveAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.LockedAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IValidateAppointment;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for validating and managing appointment operations.
 * This class handles the lifecycle of appointments including locking (reserving),
 * confirming, and unlocking (cancelling) appointments.
 */
public class ValidateAppointment implements IValidateAppointment {
    /**
     * Repository for accessing slot data.
     */
    private final SlotRepository slotRepository;

    /**
     * Repository for accessing patient data.
     */
    private final PatientRepository patientRepository;

    /**
     * Repository for accessing medical concern data.
     */
    private final MedicalConcernRepository medicalConcernRepository;

    /**
     * Repository for accessing appointment data.
     */
    private final AppointmentRepository appointmentRepository;

    /**
     * Repository for accessing doctor data.
     */
    private final DoctorRepository doctorRepository;

    private final CareTrackingRepository careTrackingRepository;

    private final GetPatientFromContext getPatientFromContext;

    /**
     * Constructs a ValidateAppointment service with the required repositories.
     *
     * @param slotRepository           Repository for accessing slot data
     * @param patientRepository        Repository for accessing patient data
     * @param medicalConcernRepository Repository for accessing medical concern data
     * @param appointmentRepository    Repository for accessing appointment data
     * @param doctorRepository         Repository for accessing doctor data
     * @param getPatientFromContext    get patient from context
     */
    public ValidateAppointment(SlotRepository slotRepository, PatientRepository patientRepository, MedicalConcernRepository medicalConcernRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext) {
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    /**
     * Locks (reserves) an appointment slot based on the provided request.
     * This method validates that the slot is authorized for the specified medical concern,
     * creates a new appointment, and saves it with a status indicating it's locked.
     *
     * @param saveAppointmentRequest The request containing all necessary information to create an appointment
     * @return A response containing the ID of the locked appointment
     * @throws ApiException If any domain validation fails or if the appointment cannot be created
     */
    public LockedAppointmentResponse lock(SaveAppointmentRequest saveAppointmentRequest) {
        try {
            Slot slot = this.slotRepository.getById(saveAppointmentRequest.slotId());
            Patient patient = this.patientRepository.getById(saveAppointmentRequest.patientId());
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(saveAppointmentRequest.medicalConcernId());
            Doctor doctor = this.doctorRepository.getById(saveAppointmentRequest.doctorId());

            UUID careTrackingId = saveAppointmentRequest.careTrackingId();
            if (careTrackingId != null) {
                CareTracking careTracking = this.careTrackingRepository.getById(careTrackingId);
                if (careTracking.getClosedAt() != null) throw new ClosedCareTrackingException();
            }

            slot.validateIfSlotIsAuthorized(medicalConcern);

            List<PreAppointmentAnswers> answers = new ArrayList<>();
            if (saveAppointmentRequest.responses() != null) {
                answers = extractedPreAppointmentAnswers(saveAppointmentRequest);
            }

            Appointment appointment = Appointment.init(slot, patient, doctor, medicalConcern, saveAppointmentRequest.time(), answers, careTrackingId);
            UUID appointmentLockedId = this.appointmentRepository.save(appointment);

            return new LockedAppointmentResponse(appointmentLockedId);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }


    /**
     * Unlocks (cancels) a previously locked appointment.
     * This method deletes the appointment with the specified ID from the repository.
     *
     * @param id The unique identifier of the appointment to unlock
     * @throws ApiException If the appointment cannot be deleted or does not exist
     */
    public void unlocked(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Appointment appointment = this.appointmentRepository.getByIdAndPatientId(id, patient.getId());
            this.appointmentRepository.delete(appointment);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /**
     * Confirms a previously locked appointment.
     * This method retrieves the appointment with the specified ID, changes its status to confirmed,
     * and updates it in the repository.
     * Note: There is a TODO to implement email notification to the patient and user.
     *
     * @param id The unique identifier of the appointment to confirm
     * @throws ApiException If the appointment cannot be confirmed or does not exist
     */
    public void confirm(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Appointment appointment = this.appointmentRepository.getByIdAndPatientId(id, patient.getId());
            appointment.confirm();
            this.appointmentRepository.confirm(appointment);
            // todo send an email to inform the patient and if it's not main account, send to user
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }


    /**
     * Extracts pre-appointment answers from the appointment request.
     * This method processes the responses provided in the appointment request,
     * retrieves the corresponding questions, and creates PreAppointmentAnswers objects.
     *
     * @param saveAppointmentRequest The request containing the responses to pre-appointment questions
     * @return A list of PreAppointmentAnswers objects
     */
    private List<PreAppointmentAnswers> extractedPreAppointmentAnswers(SaveAppointmentRequest saveAppointmentRequest) {
        List<PreAppointmentAnswers> answers = new ArrayList<>();

        List<SaveAnswersForAnAppointmentRequest> answersForAnAppointmentRequests = saveAppointmentRequest.responses();
        answersForAnAppointmentRequests.forEach(request -> {
            Question question = this.medicalConcernRepository.getQuestionById(request.questionId());
            answers.add(PreAppointmentAnswers.of(question, request.answer()));
        });

        return answers;
    }

}
