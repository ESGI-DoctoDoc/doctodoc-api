package fr.esgi.doctodocapi.presentation.doctor.manage_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.CreateMedicalConcernRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.ICreateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.IGetAllMedicalConcerns;
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
public class ManageMedicalConcernController {
    private final ICreateMedicalConcern createMedicalConcern;
    private final IGetAllMedicalConcerns getAllMedicalConcern;

    public ManageMedicalConcernController(ICreateMedicalConcern createMedicalConcern, IGetAllMedicalConcerns getAllMedicalConcern) {
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
        return this.createMedicalConcern.execute(createMedicalConcernRequest);
    }
}
