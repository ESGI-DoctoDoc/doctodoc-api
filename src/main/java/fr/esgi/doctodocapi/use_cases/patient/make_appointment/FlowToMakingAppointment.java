package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment.IFlowToMakingAppointment;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorListQuestionsResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Service managing the flow to making an appointment for a patient.
 * <p>
 * This includes retrieving medical concerns, related questions,
 * and available appointment slots based on a medical concern and date.
 * </p>
 */
public class FlowToMakingAppointment implements IFlowToMakingAppointment {
    private final MedicalConcernRepository medicalConcernRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentsAvailabilityService appointmentsAvailabilityService;

    /**
     * Constructs the service with its dependencies.
     *
     * @param medicalConcernRepository        repository for accessing medical concerns and related questions
     * @param doctorRepository                repository for retrieving doctor entities
     * @param appointmentsAvailabilityService service responsible for calculating appointment availability
     */
    public FlowToMakingAppointment(MedicalConcernRepository medicalConcernRepository,
                                   DoctorRepository doctorRepository,
                                   AppointmentsAvailabilityService appointmentsAvailabilityService) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentsAvailabilityService = appointmentsAvailabilityService;
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

    /**
     * Retrieves available appointment slots for a given medical concern on a specific date.
     *
     * @param medicalConcernId the UUID of the medical concern
     * @param date             the date for which to check availability
     * @return a list of {@link GetAppointmentAvailabilityResponse} containing available slots
     * @throws ApiException in case of a domain-related error
     */
    public List<GetAppointmentAvailabilityResponse> getAppointmentsAvailability(UUID medicalConcernId, LocalDate date) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);
            List<GetAppointmentAvailabilityResponse> appointments = this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern, date);
            return appointments.stream().sorted(Comparator.comparing(GetAppointmentAvailabilityResponse::start)).toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
