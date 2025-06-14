package fr.esgi.doctodocapi.presentation.doctor.absence;

import fr.esgi.doctodocapi.dtos.requests.doctor.absence.SaveRangeAbsenceRequest;
import fr.esgi.doctodocapi.dtos.requests.doctor.absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.absence.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.absence.GetAbsences;
import fr.esgi.doctodocapi.use_cases.doctor.absence.SaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.absence.SaveSingleDayAbsence;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public ManageAbsenceController(GetAbsences getAbsences, SaveSingleDayAbsence saveSingleDayAbsence, SaveRangeAbsence saveRangeAbsence) {
        this.getAbsences = getAbsences;
        this.saveSingleDayAbsence = saveSingleDayAbsence;
        this.saveRangeAbsence = saveRangeAbsence;
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
}
