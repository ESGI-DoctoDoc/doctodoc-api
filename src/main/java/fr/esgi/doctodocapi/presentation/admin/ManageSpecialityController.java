package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.requests.AddSpecialityRequest;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IAddSpeciality;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.IGetAllSpecialities;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing medical specialities.
 * <p>
 * This controller provides endpoints for administrators to create and list medical specialities.
 * Access is restricted to users with the {@code ROLE_ADMIN} role.
 */
@RequestMapping("doctors")
@RestController
public class ManageSpecialityController {
    private final IAddSpeciality addSpeciality;
    private final IGetAllSpecialities getAllSpecialities;

    public ManageSpecialityController(IAddSpeciality addSpeciality, IGetAllSpecialities getAllSpecialities) {
        this.addSpeciality = addSpeciality;
        this.getAllSpecialities = getAllSpecialities;
    }

    /**
     * Retrieves all available specialities.
     *
     * @return list of {@link GetSpecialityResponse} representing all stored specialities
     */
    @GetMapping("/specialities")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSpecialityResponse> getAllSpecialities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return getAllSpecialities.getAll(page, size);
    }

    /**
     * Adds a new speciality to the system.
     *
     * @param addSpecialityRequest the request body containing the name of the new speciality
     * @return the created speciality as a {@link GetSpecialityResponse}
     */
    @PostMapping("/specialities")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public GetSpecialityResponse addSpeciality(@Valid @RequestBody AddSpecialityRequest addSpecialityRequest) {
        return this.addSpeciality.execute(addSpecialityRequest);
    }
}
