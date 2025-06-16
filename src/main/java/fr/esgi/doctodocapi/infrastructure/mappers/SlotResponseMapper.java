package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.dtos.responses.doctor.slot.GetSlotResponse;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrenceType;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlotRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SlotResponseMapper {
    private static final DateTimeFormatter HOUR_MIN_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final RecurrentSlotRepository recurrentSlotRepository;

    public SlotResponseMapper(RecurrentSlotRepository recurrentSlotRepository) {
        this.recurrentSlotRepository = recurrentSlotRepository;
    }

    public List<GetSlotResponse> presentAll(List<Slot> slots) {
        return slots.stream()
                .map(this::toGetSlotResponse)
                .toList();
    }

    private GetSlotResponse toGetSlotResponse(Slot slot) {
        String recurrence = determineRecurrence(slot);
        Integer dayNumber = determineDayNumber(slot, recurrence);
        UUID recurrenceId = slot.getRecurrenceId();

        return new GetSlotResponse(
                slot.getId(),
                formatDate(slot.getDate()),
                slot.getDate().getDayOfWeek().name().toLowerCase(),
                formatTime(slot.getHoursRange().getStart()),
                formatTime(slot.getHoursRange().getEnd()),
                recurrence,
                dayNumber,
                recurrenceId
        );
    }

    private String determineRecurrence(Slot slot) {
        if (slot.getRecurrenceId() == null) {
            return RecurrenceType.NONE.getValue();
        }
        return recurrentSlotRepository.findById(slot.getRecurrenceId())
                .map(recurrentSlot -> recurrentSlot.getType().getValue().toLowerCase())
                .orElse(RecurrenceType.NONE.getValue());
    }

    private Integer determineDayNumber(Slot slot, String recurrence) {
        if (RecurrenceType.MONTHLY.getValue().equalsIgnoreCase(recurrence) && slot.getRecurrenceId() != null) {
            return slot.getDate().getDayOfMonth();
        }
        return null;
    }

    private String formatTime(LocalTime time) {
        return time.format(HOUR_MIN_FORMATTER);
    }

    private String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}