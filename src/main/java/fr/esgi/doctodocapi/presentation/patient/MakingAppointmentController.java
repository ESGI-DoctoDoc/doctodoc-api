package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.GetAppointmentAvailableResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import fr.esgi.doctodocapi.use_cases.patient.MakingAppointment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class MakingAppointmentController {
    private final MakingAppointment makingAppointment;

    public MakingAppointmentController(MakingAppointment makingAppointment) {
        this.makingAppointment = makingAppointment;
    }

    @GetMapping("patients/close-members")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetCloseMemberResponse> getCloseMembers() {
        return this.makingAppointment.getCloseMembers();
    }

    @GetMapping("patients/doctors/{id}/medical-concerns")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetMedicalConcernsResponse> getMedicalConcerns(@PathVariable UUID id) {
        return this.makingAppointment.getMedicalConcerns(id);
    }

    @GetMapping("patients/doctors/medical-concerns/{id}/questions")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorQuestionsResponse> getDoctorQuestions(@PathVariable UUID id) {
        return this.makingAppointment.getDoctorQuestions(id);
    }

    @GetMapping("patients/doctors/medical-concerns/{id}/get-appointments-availability")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetAppointmentAvailableResponse> getAppointements(@PathVariable UUID id, @RequestParam LocalDate date) {
        return this.makingAppointment.getAppointmentsAvailability(id, date);
    }
}
