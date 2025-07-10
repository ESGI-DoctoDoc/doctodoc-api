package fr.esgi.doctodocapi.presentation.doctor.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.ExceptionalSlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.MonthlySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotByIdResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IGetSlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveExceptionalSlot;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveWeeklySlots;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller responsible for managing doctor's slots (weekly, monthly, and retrieval).
 * Provides endpoints to create recurring slots and to retrieve them with pagination.
 */
@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageSlotsController {
    private static final Logger logger = LoggerFactory.getLogger(ManageSlotsController.class);

    private final ISaveWeeklySlots saveWeeklySlots;
    private final ISaveMonthlySlots saveMonthlySlots;
    private final IGetSlots getSlots;
    private final ISaveExceptionalSlot saveExceptionalSlot;

    public ManageSlotsController(ISaveWeeklySlots saveWeeklySlots, ISaveMonthlySlots saveMonthlySlots, IGetSlots getSlots, ISaveExceptionalSlot saveExceptionalSlot) {
        this.saveWeeklySlots = saveWeeklySlots;
        this.saveMonthlySlots = saveMonthlySlots;
        this.getSlots = getSlots;
        this.saveExceptionalSlot = saveExceptionalSlot;
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

    @PostMapping("/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public GetSlotResponse createExceptionalSlot(@Valid @RequestBody ExceptionalSlotRequest request) {
        logger.info("[POST /doctors/slots] Received payload: {}", request);
        return this.saveExceptionalSlot.execute(request);
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
    public List<GetSlotResponse> getAllSlots(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1000") int size, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return this.getSlots.getAll(page, size, startDate);
    }

    @GetMapping("slots/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetSlotByIdResponse getSlotById(@PathVariable UUID id) {
        return this.getSlots.getSlotById(id);
    }
}
