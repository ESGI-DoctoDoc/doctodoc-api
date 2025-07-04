package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlotRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.day_of_month.DayOfMonth;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.MonthlySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case for creating monthly recurring slots for a doctor.
 * <p>
 * This service creates slots on a specific day number of each month,
 * between a given start and end date. It also ensures no overlapping slots
 * with the same medical concerns already exist.
 */
public class SaveMonthlySlots implements ISaveMonthlySlots {
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final RecurrentSlotRepository recurrentSlotRepository;
    private final SlotResponseMapper slotResponseMapper;
    private final DoctorRepository doctorRepository;

    public SaveMonthlySlots(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, RecurrentSlotRepository recurrentSlotRepository, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.recurrentSlotRepository = recurrentSlotRepository;
        this.slotResponseMapper = slotResponseMapper;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Executes the creation of monthly slots.
     *
     * @param request The request containing recurrence information and slot details
     * @return A list of created slot responses
     * @throws ApiException if a domain-level validation fails
     */
    public List<GetSlotResponse> execute(MonthlySlotRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            LocalDate startDate = request.start();
            LocalDate endDate = request.end();
            DateRange dateRange = DateRange.of(startDate, endDate);

            List<MedicalConcern> concerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            DayOfMonth dayOfMonth = new DayOfMonth(request.dayNumber());
            int targetDay = dayOfMonth.getDay();

            LocalDate current = firstOccurrence(dateRange.getStart(), targetDay);
            List<Slot> newSlots = new ArrayList<>();
            List<Slot> allExistingSlots = new ArrayList<>(
                    this.slotRepository.findAllByDoctorIdAndDateAfter(doctor.getId(), startDate)
            );

            while (!current.isAfter(dateRange.getEnd())) {
                Slot newSlot = Slot.createRecurrence(current, request.startHour(), request.endHour(), concerns, null);
                newSlot.validateAgainstOverlaps(allExistingSlots);

                doctor.getCalendar().addSlot(newSlot);
                allExistingSlots.add(newSlot);
                newSlots.add(newSlot);

                current = nextOccurrence(current, targetDay);
            }

            if (!newSlots.isEmpty()) {
                RecurrentSlot recurrentSlot = RecurrentSlot.createMonthly(startDate, endDate);
                RecurrentSlot savedRecurrentSlot = this.recurrentSlotRepository.save(recurrentSlot);

                for (Slot slot : newSlots) {
                    slot.setRecurrenceId(savedRecurrentSlot.getId());
                }

                List<Slot> savedSlots = this.slotRepository.saveAll(newSlots, doctor.getId());
                return this.slotResponseMapper.presentAll(savedSlots);
            }

            return List.of();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private LocalDate firstOccurrence(LocalDate startDate, int targetDay) {
        LocalDate current = startDate;
        while (current.getDayOfMonth() != targetDay) {
            current = current.plusDays(1);
            if (current.getDayOfMonth() > targetDay) {
                current = current.withDayOfMonth(1).plusMonths(1).withDayOfMonth(targetDay);
                break;
            }
        }
        return applyDay(current, targetDay);
    }

    private LocalDate nextOccurrence(LocalDate current, int targetDay) {
        return applyDay(current.plusMonths(1), targetDay);
    }

    private LocalDate applyDay(LocalDate date, int targetDay) {
        int safeDay = Math.min(targetDay, date.lengthOfMonth());
        return date.withDayOfMonth(safeDay);
    }
}