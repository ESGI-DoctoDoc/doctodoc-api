package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.domain.use_cases.patient.ports.in.IFlowToMakingAppointment;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller to support patient workflow for making medical appointments.
 * All endpoints require authenticated users with role 'PATIENT'.
 */
@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class FlowToMakingAppointmentController {
    private final IFlowToMakingAppointment flowToMakingAppointment;

    public FlowToMakingAppointmentController(IFlowToMakingAppointment flowToMakingAppointment) {
        this.flowToMakingAppointment = flowToMakingAppointment;
    }

    /**
     * Retrieve medical concerns related to a specific doctor.
     *
     * @param id UUID of the doctor
     * @return list of medical concerns available for the doctor
     */
    @GetMapping("patients/doctors/{id}/medical-concerns")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetMedicalConcernsResponse> getMedicalConcerns(@PathVariable UUID id) {
        return this.flowToMakingAppointment.getMedicalConcerns(id);
    }

    /**
     * Retrieve specific questions related to a medical concern.
     *
     * @param id UUID of the medical concern
     * @return list of questions to ask the patient
     */
    @GetMapping("patients/doctors/medical-concerns/{id}/questions")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorQuestionsResponse> getMedicalConcernQuestions(@PathVariable UUID id) {
        return this.flowToMakingAppointment.getMedicalConcernQuestions(id);
    }

    /**
     * Retrieve available appointment slots for a specific medical concern on a given date.
     *
     * @param id   UUID of the medical concern
     * @param date desired appointment date
     * @return list of available appointment slots
     */
    @GetMapping("patients/doctors/medical-concerns/{id}/get-appointments-availability")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAppointmentAvailabilityResponse> getAppointements(@PathVariable UUID id, @RequestParam LocalDate date) {
        return this.flowToMakingAppointment.getAppointmentsAvailability(id, date);
    }
}
