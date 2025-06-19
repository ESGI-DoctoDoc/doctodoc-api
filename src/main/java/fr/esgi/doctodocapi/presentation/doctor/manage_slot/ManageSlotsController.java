package fr.esgi.doctodocapi.presentation.doctor.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.MonthlySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IGetAllSlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveWeeklySlots;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller responsible for managing doctor's slots (weekly, monthly, and retrieval).
 * Provides endpoints to create recurring slots and to retrieve them with pagination.
 */
@RestController
@RequestMapping("doctors")
public class ManageSlotsController {
    private final ISaveWeeklySlots saveWeeklySlots;
    private final ISaveMonthlySlots saveMonthlySlots;
    private final IGetAllSlots getAllSlots;

    public ManageSlotsController(ISaveWeeklySlots saveWeeklySlots, ISaveMonthlySlots saveMonthlySlots, IGetAllSlots getAllSlots) {
        this.saveWeeklySlots = saveWeeklySlots;
        this.saveMonthlySlots = saveMonthlySlots;
        this.getAllSlots = getAllSlots;
    }

    /**
     * Creates a set of weekly recurring slots for the current doctor.
     *
     * @param request The request payload containing recurrence settings and slot definitions
     * @return A list of the created slots
     */
    @PostMapping("/slots/weekly")
    @ResponseStatus(HttpStatus.CREATED)
    public List<GetSlotResponse> createWeeklySlots(@Valid @RequestBody WeeklySlotRequest request) {
        return saveWeeklySlots.execute(request);
    }

    /**
     * Creates a set of monthly recurring slots for the current doctor.
     *
     * @param request The request payload containing recurrence settings and slot definitions
     * @return A list of the created slots
     */
    @PostMapping("/slots/monthly")
    @ResponseStatus(HttpStatus.CREATED)
    public List<GetSlotResponse> createMonthlySlots(@Valid @RequestBody MonthlySlotRequest request) {
        return saveMonthlySlots.execute(request);
    }


    /**
     * Retrieves all the doctor's slots starting from a specified date,
     * with optional pagination.
     *
     * @param page      The page number to retrieve (default is 0)
     * @param size      The number of items per page (default is 10)
     * @param startDate The minimum date (inclusive) to start listing slots
     * @return A list of slots matching the filter criteria
     */
    @GetMapping("/slots")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetSlotResponse> getAllSlots(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return this.getAllSlots.getAll(page, size, startDate);
    }
}
