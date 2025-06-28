package fr.esgi.doctodocapi.presentation.doctor.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveRangeAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IDeleteAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IGetAbsences;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveSingleDayAbsence;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageAbsenceController {
    private final IDeleteAbsence deleteAbsence;
    private final IGetAbsences getAbsences;
    private final ISaveRangeAbsence saveRangeAbsence;
    private final ISaveSingleDayAbsence saveSingleDayAbsence;

    public ManageAbsenceController(IDeleteAbsence deleteAbsence, IGetAbsences getAbsences, ISaveRangeAbsence saveRangeAbsence, ISaveSingleDayAbsence saveSingleDayAbsence) {
        this.deleteAbsence = deleteAbsence;
        this.getAbsences = getAbsences;
        this.saveRangeAbsence = saveRangeAbsence;
        this.saveSingleDayAbsence = saveSingleDayAbsence;
    }


    /**
     * Retrieves all absences for the currently authenticated doctor.
     *
     * @return a list of {@link GetAbsenceResponse}
     */
    @GetMapping("absences")
    @ResponseStatus(HttpStatus.OK)
    public List<GetAbsenceResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return this.getAbsences.execute(page, size, startDate);
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
