package fr.esgi.doctodocapi.use_cases.doctor.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.MonthlySlotRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.slot.GetSlotResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveMonthlySlots {
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public SaveMonthlySlots(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public GetSlotResponse execute(MonthlySlotRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);
            List<MedicalConcern> concerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            int targetDay = request.dayNumber();
            LocalDate end = request.end();
            LocalDate current = firstOccurrence(targetDay);

            List<Slot> slots = new ArrayList<>();
            while (!current.isAfter(end)) {
                slots.add(Slot.create(current, request.startHour(), request.endHour(), doctor.getId(), concerns));
                current = nextOccurrence(current, targetDay);
            }

            this.slotRepository.saveAll(slots);
            return new GetSlotResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private LocalDate firstOccurrence(int targetDay) {
        LocalDate today = LocalDate.now();

        if (today.getDayOfMonth() <= targetDay) {
            return applyDay(today, targetDay);
        }

        return applyDay(today.plusMonths(1), targetDay);
    }

    private LocalDate nextOccurrence(LocalDate current, int targetDay) {
        return applyDay(current.plusMonths(1), targetDay);
    }

    private LocalDate applyDay(LocalDate date, int targetDay) {
        int safeDay = Math.min(targetDay, date.lengthOfMonth());
        return date.withDayOfMonth(safeDay);
    }
}