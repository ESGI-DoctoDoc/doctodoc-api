package fr.esgi.doctodocapi.presentation.doctor.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.SlotRequest;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.use_cases.doctor.slot.SaveRecurringSlots;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class ManageSlotsController {
    private final SaveRecurringSlots saveRecurringSlots;

    public ManageSlotsController(SaveRecurringSlots saveRecurringSlots) {
        this.saveRecurringSlots = saveRecurringSlots;
    }

    @PostMapping("/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Slot> generateRecurringSlots(@Valid @RequestBody SlotRequest slotRequest) {
        return this.saveRecurringSlots.execute(slotRequest);
    }
}
