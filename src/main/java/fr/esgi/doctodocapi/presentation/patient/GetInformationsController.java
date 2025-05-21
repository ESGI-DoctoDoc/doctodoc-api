package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.GetBasicPatientInfo;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.use_cases.patient.GetInformations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetInformationsController {
    private final GetInformations getInformations;

    public GetInformationsController(GetInformations getInformations) {
        this.getInformations = getInformations;
    }

    @GetMapping("patients/user/infos")
    @ResponseStatus(value = HttpStatus.OK)
    public GetBasicPatientInfo getBasicPatientInfo() {
        return this.getInformations.getBasicPatientInfo();
    }

    @GetMapping("patients/close-members")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetCloseMemberResponse> getCloseMembers() {
        return this.getInformations.getCloseMembers();
    }
}
