package fr.esgi.doctodocapi.presentation.patient.manage_account;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.UpdateProfileRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IManageProfile;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing patient profiles.
 * Only accessible to authenticated users with the role 'ROLE_PATIENT'.
 */
@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients/profile")
public class ManageProfileController {
    private final IManageProfile manageProfile;

    public ManageProfileController(IManageProfile manageProfile) {
        this.manageProfile = manageProfile;
    }

    /**
     * Updates the profile of the authenticated patient.
     *
     * @param request The profile update request containing new patient information
     * @return A response DTO with the updated profile data
     */
    @PutMapping
    public GetProfileResponse update(@Valid @RequestBody UpdateProfileRequest request) {
        return this.manageProfile.update(request);
    }
}
