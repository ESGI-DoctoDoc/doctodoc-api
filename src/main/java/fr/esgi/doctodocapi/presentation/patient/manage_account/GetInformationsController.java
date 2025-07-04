package fr.esgi.doctodocapi.presentation.patient.manage_account;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IGetInformations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing endpoints to retrieve patient-related information.
 * Access restricted to authenticated users with 'PATIENT' role.
 */
@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetInformationsController {
    private final IGetInformations getInformations;

    public GetInformationsController(IGetInformations getInformations) {
        this.getInformations = getInformations;
    }

    /**
     * Returns basic information of the currently authenticated patient.
     *
     * @return basic patient information DTO
     */
    @GetMapping("patients/user/infos")
    @ResponseStatus(value = HttpStatus.OK)
    public GetProfileResponse getBasicPatientInfo() {
        return this.getInformations.getBasicPatientInfo();
    }
}
