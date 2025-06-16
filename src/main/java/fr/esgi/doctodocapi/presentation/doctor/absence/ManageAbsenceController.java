package fr.esgi.doctodocapi.presentation.doctor.absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveRangeAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.manage_absence.DeleteAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.manage_absence.GetAbsences;
import fr.esgi.doctodocapi.use_cases.doctor.manage_absence.SaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.manage_absence.SaveSingleDayAbsence;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing doctor absences.
 * <p>
 * Provides endpoints to retrieve, create single-day absences and ranged absences
 * for the currently authenticated doctor.
 */
@RestController
@RequestMapping("doctors")
public class ManageAbsenceController {
    private final GetAbsences getAbsences;
    private final SaveSingleDayAbsence saveSingleDayAbsence;
    private final SaveRangeAbsence saveRangeAbsence;
    private final DeleteAbsence deleteAbsence;

    public ManageAbsenceController(GetAbsences getAbsences, SaveSingleDayAbsence saveSingleDayAbsence, SaveRangeAbsence saveRangeAbsence, DeleteAbsence deleteAbsence) {
        this.getAbsences = getAbsences;
        this.saveSingleDayAbsence = saveSingleDayAbsence;
        this.saveRangeAbsence = saveRangeAbsence;
        this.deleteAbsence = deleteAbsence;
    }

    /**
     * Retrieves all absences for the currently authenticated doctor.
     *
     * @return a list of {@link GetAbsenceResponse}
     */
    @GetMapping("absences")
    @ResponseStatus(HttpStatus.OK)
    public List<GetAbsenceResponse> getAll() {
        return this.getAbsences.execute();
    }

    /**
     * Creates a new single-day absence for the authenticated doctor.
     *
     * @param request the request payload containing single-day absence details
     * @return the created absence
     */
    @PostMapping("absences/single-day")
    @ResponseStatus(HttpStatus.CREATED)
    public GetAbsenceResponse createSingleDay(@Valid @RequestBody SaveSingleDayAbsenceRequest request) {
        return this.saveSingleDayAbsence.execute(request);
    }

    /**
     * Creates a new ranged absence (multiple days, optionally with hours) for the authenticated doctor.
     *
     * @param request the request payload containing ranged absence details
     * @return the created absence
     */
    @PostMapping("absences/range")
    @ResponseStatus(HttpStatus.CREATED)
    public GetAbsenceResponse createRange(@Valid @RequestBody SaveRangeAbsenceRequest request) {
        return this.saveRangeAbsence.execute(request);
    }

    /**
     * Deletes an existing absence for the authenticated doctor.
     *
     * @param id the UUID of the absence to delete
     */
    @DeleteMapping("absences/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        this.deleteAbsence.execute(id);
    }
}
