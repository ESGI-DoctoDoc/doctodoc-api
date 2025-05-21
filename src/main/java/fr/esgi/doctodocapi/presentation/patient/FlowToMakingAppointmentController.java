package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import fr.esgi.doctodocapi.use_cases.patient.FlowToMakingAppointment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class FlowToMakingAppointmentController {
    private final FlowToMakingAppointment flowToMakingAppointment;

    public FlowToMakingAppointmentController(FlowToMakingAppointment flowToMakingAppointment) {
        this.flowToMakingAppointment = flowToMakingAppointment;
    }

    @GetMapping("patients/doctors/{id}/medical-concerns")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetMedicalConcernsResponse> getMedicalConcerns(@PathVariable UUID id) {
        return this.flowToMakingAppointment.getMedicalConcerns(id);
    }

    @GetMapping("patients/doctors/medical-concerns/{id}/questions")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorQuestionsResponse> getMedicalConcernQuestions(@PathVariable UUID id) {
        return this.flowToMakingAppointment.getMedicalConcernQuestions(id);
    }

    @GetMapping("patients/doctors/medical-concerns/{id}/get-appointments-availability")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAppointmentAvailabilityResponse> getAppointements(@PathVariable UUID id, @RequestParam LocalDate date) {
        return this.flowToMakingAppointment.getAppointmentsAvailability(id, date);
    }
}
