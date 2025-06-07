package fr.esgi.doctodocapi.use_cases.doctor.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.SlotRequest;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotService;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveRecurringSlots {
    private final SlotRepository slotRepository;
    private final SlotService slotService;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public SaveRecurringSlots(SlotRepository slotRepository, SlotService slotService, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.slotRepository = slotRepository;
        this.slotService = slotService;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public List<Slot> execute(SlotRequest slotRequest) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);

            List<Slot> slots = this.slotService.generateRecurringSlots(slotRequest, doctor);
            return this.slotRepository.saveAll(slots);
        } catch(DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
