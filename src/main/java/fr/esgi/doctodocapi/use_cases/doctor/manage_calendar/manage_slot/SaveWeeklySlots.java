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
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveWeeklySlots;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

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
public class SaveWeeklySlots implements ISaveWeeklySlots {

    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final RecurrentSlotRepository recurrentSlotRepository;
    private final SlotResponseMapper slotResponseMapper;
    private final DoctorRepository doctorRepository;

    public SaveWeeklySlots(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, RecurrentSlotRepository recurrentSlotRepository, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.recurrentSlotRepository = recurrentSlotRepository;
        this.slotResponseMapper = slotResponseMapper;
        this.doctorRepository = doctorRepository;
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
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            LocalDate startDate = request.start();
            LocalDate endDate = request.end();
            DateRange dateRange = DateRange.of(startDate, endDate);

            List<MedicalConcern> concerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());
            if (concerns == null || concerns.contains(null)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "medical-concerns.invalid", "Invalid or missing medical concerns");
            }

            LocalDate current = alignToDayOfWeek(dateRange.getStart(), request.day());

            List<Slot> existingSlots = new ArrayList<>(
                    this.slotRepository.findAllByDoctorIdAndDateAfter(doctor.getId(), startDate)
            );

            while (!current.isAfter(dateRange.getEnd())) {
                Slot newSlot = Slot.createRecurrence(current, request.startHour(), request.endHour(), concerns, null);
                newSlot.validateAgainstOverlaps(existingSlots);

                doctor.getCalendar().addSlot(newSlot);
                existingSlots.add(newSlot);

                current = current.plusWeeks(1);
            }

            List<Slot> createdSlots = doctor.getCalendar().getSlots();

            if (!createdSlots.isEmpty()) {
                RecurrentSlot recurrentSlot = RecurrentSlot.createWeekly(startDate, endDate);
                RecurrentSlot savedRecurrentSlot = this.recurrentSlotRepository.save(recurrentSlot);

                for (Slot slot : createdSlots) {
                    slot.setRecurrenceId(savedRecurrentSlot.getId());
                }

                List<Slot> savedSlots = this.slotRepository.saveAll(createdSlots, doctor.getId());
                return this.slotResponseMapper.presentAll(savedSlots);
            }

            return List.of();

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