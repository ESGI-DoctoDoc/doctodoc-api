package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.GetAppointmentDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients")
public class GetAppointmentDetailController {
    private final GetAppointmentDetail getAppointmentDetail;

    public GetAppointmentDetailController(GetAppointmentDetail getAppointmentDetail) {
        this.getAppointmentDetail = getAppointmentDetail;
    }

    @GetMapping(value = "/appointments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public GetAppointmentDetailedResponse getAppointmentResponse(@PathVariable UUID id) {
        return this.getAppointmentDetail.get(id);
    }
}
