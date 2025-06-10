package fr.esgi.doctodocapi.use_cases.doctor.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.slot.GetSlotResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlotRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case for creating weekly recurring slots for a doctor.
 * <p>
 * This service generates slots for a given day of the week between a start and end date,
 * associates them with a list of medical concerns, and checks for overlaps.
 * A recurrent slot entry is saved to group the weekly recurrence.
 */
@Service
public class SaveWeeklySlots {
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final RecurrentSlotRepository recurrentSlotRepository;
    private final SlotResponseMapper slotResponseMapper;

    public SaveWeeklySlots(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, RecurrentSlotRepository recurrentSlotRepository, SlotResponseMapper slotResponseMapper) {
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.recurrentSlotRepository = recurrentSlotRepository;
        this.slotResponseMapper = slotResponseMapper;
    }

    /**
     * Executes the creation of weekly slots.
     *
     * @param request The request containing recurrence information and slot details
     * @return A list of created slot responses
     * @throws ApiException if a domain-level validation fails
     */
    public List<GetSlotResponse> execute(WeeklySlotRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);
            List<MedicalConcern> concerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            LocalDate current = alignToDayOfWeek(request.start(), request.day());
            LocalDate end = request.end();

            List<Slot> allSlotsToCheck = new ArrayList<>(this.slotRepository.findAllByDoctorIdAndDateAfter(doctor.getId(), request.start()));
            List<Slot> newSlots = new ArrayList<>();

            while (!current.isAfter(end)) {
                Slot newSlot = Slot.createRecurrence(current, request.startHour(), request.endHour(), doctor.getId(), concerns, null);

                newSlot.validateAgainstOverlaps(allSlotsToCheck);
                newSlots.add(newSlot);
                allSlotsToCheck.add(newSlot);
                
                current = current.plusWeeks(1);
            }

            RecurrentSlot recurrentSlot = RecurrentSlot.createWeekly(request.start(), request.end());
            RecurrentSlot savedRecurrentSlot = this.recurrentSlotRepository.save(recurrentSlot);

            for (Slot slot : newSlots) {
                slot.setRecurrenceId(savedRecurrentSlot.getId());
            }

            List<Slot> slots = this.slotRepository.saveAll(newSlots);
            return this.slotResponseMapper.presentAll(slots);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private LocalDate alignToDayOfWeek(LocalDate date, DayOfWeek targetDay) {
        while (date.getDayOfWeek() != targetDay) {
            date = date.plusDays(1);
        }
        return date;
    }
}