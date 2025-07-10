package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetCareTrackingForAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorListQuestionsResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.get_appointment_availability_response.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.get_appointment_availability_response.GetTodayAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IFlowToMakingAppointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service managing the flow to making an appointment for a patient.
 * <p>
 * This includes retrieving medical concerns, related questions,
 * and available appointment slots based on a medical concern and date.
 * </p>
 */
public class FlowToMakingAppointment implements IFlowToMakingAppointment {
    private static final Logger logger = LoggerFactory.getLogger(FlowToMakingAppointment.class);

    private final MedicalConcernRepository medicalConcernRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final AppointmentsAvailabilityService appointmentsAvailabilityService;
    private final SlotRepository slotRepository;
    private final AbsenceRepository absenceRepository;

    /**
     * Constructs the service with its dependencies.
     *
     * @param medicalConcernRepository        repository for accessing medical concerns and related questions
     * @param doctorRepository                repository for retrieving doctor entities
     * @param appointmentsAvailabilityService service responsible for calculating appointment availability
     */
    public FlowToMakingAppointment(MedicalConcernRepository medicalConcernRepository, DoctorRepository doctorRepository, DoctorSubscriptionRepository doctorSubscriptionRepository, CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentsAvailabilityService appointmentsAvailabilityService, SlotRepository slotRepository, AbsenceRepository absenceRepository) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.appointmentsAvailabilityService = appointmentsAvailabilityService;
        this.slotRepository = slotRepository;
        this.absenceRepository = absenceRepository;
    }

    /**
     * Retrieves the list of medical concerns associated with a given doctor.
     *
     * @param doctorId the UUID of the doctor
     * @return a list of {@link GetMedicalConcernsResponse} representing medical concerns
     * @throws ApiException in case of a domain-related error
     */
    public List<GetMedicalConcernsResponse> getMedicalConcerns(UUID doctorId) {
        try {
            Doctor doctor = this.doctorRepository.getById(doctorId);

            Optional<DoctorSubscription> doctorSubscription = this.doctorSubscriptionRepository.findActivePaidSubscriptionByDoctorId(doctor.getId());
            if (doctorSubscription.isEmpty()) return List.of();

            List<MedicalConcern> medicalConcerns = this.medicalConcernRepository.getMedicalConcerns(doctor);

            return medicalConcerns.stream()
                    .map(medicalConcern -> new GetMedicalConcernsResponse(medicalConcern.getId(), medicalConcern.getName()))
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    /**
     * Retrieves the list of questions related to a specific medical concern.
     * Supports both generic and list-type questions.
     *
     * @param medicalConcernId the UUID of the medical concern
     * @return a list of {@link GetDoctorQuestionsResponse} or {@link GetDoctorListQuestionsResponse}
     * @throws ApiException in case of a domain-related error
     */
    public List<GetDoctorQuestionsResponse> getMedicalConcernQuestions(UUID medicalConcernId) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);
            List<Question> doctorQuestions = this.medicalConcernRepository.getDoctorQuestions(medicalConcern);

            List<GetDoctorQuestionsResponse> questionsResponses = new ArrayList<>();

            doctorQuestions.forEach(question -> {
                if (question.getType() == QuestionType.LIST) {
                    questionsResponses.add(new GetDoctorListQuestionsResponse(
                            question.getId().toString(),
                            question.getType().getValue(),
                            question.getQuestion(),
                            question.isMandatory(),
                            question.getOptions()));
                } else {
                    questionsResponses.add(new GetDoctorQuestionsResponse(
                            question.getId().toString(),
                            question.getType().getValue(),
                            question.getQuestion(),
                            question.isMandatory()));
                }
            });

            return questionsResponses;
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public List<GetCareTrackingForAppointmentResponse> getCareTracking() {
        try {
            Patient patient = this.getPatientFromContext.get();
            List<CareTracking> careTrackings = this.careTrackingRepository.findAllOpenedByPatientId(patient.getId());

            return careTrackings.stream()
                    .map(careTracking -> new GetCareTrackingForAppointmentResponse(careTracking.getId(), careTracking.getCaseName(), careTracking.getDescription()))
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }


    /**
     * Retrieves available appointment slots for a given medical concern on a specific date.
     *
     * @param medicalConcernId the UUID of the medical concern
     * @param date             the date for which to check availability
     * @return a list of {@link GetTodayAppointmentAvailabilityResponse} containing available slots
     * @throws ApiException in case of a domain-related error
     */
    public GetAppointmentAvailabilityResponse getAppointmentsAvailability(UUID medicalConcernId, LocalDate date) {
        try {

            List<GetTodayAppointmentAvailabilityResponse> appointments = new ArrayList<>();

            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);

            Optional<DoctorSubscription> doctorSubscription = this.doctorSubscriptionRepository.findActivePaidSubscriptionByDoctorId(medicalConcern.getDoctorId());
            if (doctorSubscription.isEmpty()) return new GetAppointmentAvailabilityResponse(appointments, null, null);

            UUID doctorId = doctorSubscription.get().getDoctorId();

            LocalDateTime endDate = doctorSubscription.get().getEndDate();

            if (date.isEqual(endDate.toLocalDate()) || date.isBefore(endDate.toLocalDate())) {
                logger.info("Get available appointments for the requested date {}.", date);

                appointments = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern, date);
                LocalDate previousOne = findNextOrPreviousSlot(date, doctorId, medicalConcern, false);
                LocalDate nextOne = findNextOrPreviousSlot(date, doctorId, medicalConcern, true);

                List<GetTodayAppointmentAvailabilityResponse> today = appointments.stream().sorted(Comparator.comparing(GetTodayAppointmentAvailabilityResponse::start)).toList();

                return new GetAppointmentAvailabilityResponse(today, previousOne, nextOne);
            } else {
                logger.info("No available appointments: the requested date {} is after the doctor's subscription end date {}.", date, endDate.toLocalDate());
                LocalDate previousOne = findNextOrPreviousSlot(endDate.toLocalDate(), doctorId, medicalConcern, false);
                return new GetAppointmentAvailabilityResponse(appointments, previousOne, null);
            }

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private LocalDate findNextOrPreviousSlot(
            LocalDate startDate,
            UUID doctorId,
            MedicalConcern medicalConcern,
            boolean searchForward
    ) {
        LocalDate searchDate = startDate;

        while (true) {
            List<Slot> slots = searchForward
                    ? this.slotRepository.getByNextDateAndDoctorId(searchDate, doctorId)
                    : this.slotRepository.getByPreviousDateAndDoctorId(searchDate, doctorId);

            if (slots.isEmpty()) {
                logger.info("No {} one found", searchForward ? "next" : "previous");
                return null;
            }

            LocalDate candidateDate = slots.getFirst().getDate();

            if (!searchForward && candidateDate.isBefore(LocalDate.now())) {
                logger.info("Reached past limit without finding suitable slots");
                return null;
            }

            List<Absence> absences = this.absenceRepository.findAllByDoctorIdAndDate(doctorId, candidateDate);

            boolean found = slots.stream().anyMatch(slot -> {
                List<GetTodayAppointmentAvailabilityResponse> availability =
                        this.appointmentsAvailabilityService.extractAppointmentAvailable(medicalConcern, slot, absences);
                return availability.size() > 1;
            });

            if (found) {
                logger.info("Found {} one : {}", searchForward ? "next" : "previous", candidateDate);
                return candidateDate;
            }

            searchDate = candidateDate;
        }
    }

}
