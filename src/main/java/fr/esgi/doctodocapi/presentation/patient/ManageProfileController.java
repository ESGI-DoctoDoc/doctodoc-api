package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.patient.UpdateProfileRequest;
import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ManageProfile;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients/profile")
public class ManageProfileController {
    private final ManageProfile manageProfile;

    public ManageProfileController(ManageProfile manageProfile) {
        this.manageProfile = manageProfile;
    }

    @PutMapping
    public GetProfileResponse update(@Valid @RequestBody UpdateProfileRequest request) {
        return this.manageProfile.update(request);
    }
}
