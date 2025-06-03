package fr.esgi.doctodocapi.presentation.doctor.medical_concern;

import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.CreateMedicalConcernRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern.GetMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.medical_concern.CreateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.doctor.medical_concern.GetAllMedicalConcerns;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing medical concerns related to doctors.
 * Provides endpoints to retrieve and create medical concerns for the authenticated doctor.
 */
@RestController
@RequestMapping("doctors")
public class ManageMedicalConcern {
    private final CreateMedicalConcern createMedicalConcern;
    private final GetAllMedicalConcerns getAllMedicalConcern;

    public ManageMedicalConcern(CreateMedicalConcern createMedicalConcern, GetAllMedicalConcerns getAllMedicalConcern) {
        this.createMedicalConcern = createMedicalConcern;
        this.getAllMedicalConcern = getAllMedicalConcern;
    }


    /**
     * Retrieves all medical concerns associated with the currently authenticated doctor.
     *
     * @return list of medical concern DTOs with their associated questions
     */
    @GetMapping("/medical-concerns")
    @ResponseStatus(HttpStatus.OK)
    public List<GetMedicalConcernResponse> getAll() {
        return this.getAllMedicalConcern.execute();
    }

    /**
     * Creates a new medical concern for the currently authenticated doctor.
     *
     * @param createMedicalConcernRequest the request body containing medical concern information
     * @return the created medical concern as a DTO
     */
    @PostMapping("/medical-concerns")
    @ResponseStatus(HttpStatus.CREATED)
    public GetMedicalConcernResponse create(@Valid @RequestBody CreateMedicalConcernRequest createMedicalConcernRequest) {
        return  this.createMedicalConcern.execute(createMedicalConcernRequest);
    }
}
