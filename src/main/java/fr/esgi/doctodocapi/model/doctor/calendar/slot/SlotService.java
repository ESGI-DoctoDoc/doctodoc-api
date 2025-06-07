package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.dtos.requests.doctor.slot.SlotRequest;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SlotService {
    private final MedicalConcernRepository medicalConcernRepository;

    public SlotService(MedicalConcernRepository medicalConcernRepository) {
        this.medicalConcernRepository = medicalConcernRepository;
    }

    public List<Slot> generateRecurringSlots(SlotRequest slotRequest, User doctor) {
        RecurrenceType recurrence = RecurrenceType.fromValue(slotRequest.recurrence());

        List<MedicalConcern> medicalConcerns = this.medicalConcernRepository.findAllById(slotRequest.medicalConcerns());

        return switch (recurrence) {
            case WEEKLY -> generateWeeklySlots(doctor.getId(), slotRequest, medicalConcerns);
            case MONTHLY -> generateMonthlySlots(doctor.getId(), slotRequest, medicalConcerns);
            default -> throw new RecurrenceTypeNotFound();
        };
    }

    public List<Slot> generateWeeklySlots(UUID doctorId, SlotRequest request, List<MedicalConcern> concerns) {
        List<Slot> slots = new ArrayList<>();
        LocalDate current = alignToDayOfWeek(request.start(), request.day());
        LocalDate end = request.end();

        while (!current.isAfter(end)) {
            slots.add(Slot.create(current, request.startHour(), request.endHour(), doctorId, concerns));
            current = current.plusWeeks(1);
        }
        return slots;
    }

    public List<Slot> generateMonthlySlots(UUID doctorId, SlotRequest request, List<MedicalConcern> concerns) {
        List<Slot> slots = new ArrayList<>();
        LocalDate current = computeFirstMonthlyOccurrence(LocalDate.now(), request.dayNumber());
        LocalDate end = request.end();

        while (!current.isAfter(end)) {
            slots.add(Slot.create(current, request.startHour(), request.endHour(), doctorId, concerns));
            current = current.plusMonths(1);
        }
        return slots;
    }

    private LocalDate computeFirstMonthlyOccurrence(LocalDate baseDate, int dayOfMonth) {
        if (baseDate.getDayOfMonth() <= dayOfMonth) {
            return baseDate.withDayOfMonth(Math.min(dayOfMonth, baseDate.lengthOfMonth()));
        }
        LocalDate nextMonth = baseDate.plusMonths(1);

        return nextMonth.withDayOfMonth(Math.min(dayOfMonth, nextMonth.lengthOfMonth()));
    }

    private LocalDate alignToDayOfWeek(LocalDate start, DayOfWeek targetDay) {
        while (start.getDayOfWeek() != targetDay) {
            start = start.plusDays(1);
        }

        return start;
    }
}