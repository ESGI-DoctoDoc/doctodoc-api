package fr.esgi.doctodocapi.presentation.doctor.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.MonthlySlotRequest;
import fr.esgi.doctodocapi.dtos.requests.doctor.slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.slot.GetSlotResponse;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.use_cases.doctor.slot.SaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.doctor.slot.SaveWeeklySlots;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class ManageSlotsController {
    private final SaveWeeklySlots saveWeeklySlots;
    private final SaveMonthlySlots saveMonthlySlots;

    public ManageSlotsController(SaveWeeklySlots saveWeeklySlots, SaveMonthlySlots saveMonthlySlots) {
        this.saveWeeklySlots = saveWeeklySlots;
        this.saveMonthlySlots = saveMonthlySlots;
    }

    @PostMapping("/slots/weekly")
    @ResponseStatus(HttpStatus.CREATED)
    public GetSlotResponse createWeeklySlots(@Valid @RequestBody WeeklySlotRequest request) {
        return saveWeeklySlots.execute(request);
    }

    @PostMapping("/slots/monthly")
    @ResponseStatus(HttpStatus.CREATED)
    public GetSlotResponse createMonthlySlots(@Valid @RequestBody MonthlySlotRequest request) {
        return saveMonthlySlots.execute(request);
    }
}
