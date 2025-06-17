package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information;

import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.OpeningHoursResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class OpeningHoursCalculator {
    public List<OpeningHoursResponse> getOpeningHoursOfCurrentWeek(List<Slot> slots) {
        Map<DayOfWeek, List<Slot>> slotsByDay = getSlotsOfCurrentWeekGroupedByDay(slots);

        List<OpeningHoursResponse> responses = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            List<Slot> daySlots = slotsByDay.getOrDefault(day, Collections.emptyList());
            responses.add(buildOpeningHoursForDay(day, daySlots));
        }

        return responses;
    }

    private Map<DayOfWeek, List<Slot>> getSlotsOfCurrentWeekGroupedByDay(List<Slot> slots) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        return slots.stream()
                .filter(slot -> !slot.getDate().isBefore(startOfWeek) && !slot.getDate().isAfter(endOfWeek))
                .collect(Collectors.groupingBy(slot -> slot.getDate().getDayOfWeek()));
    }

    private OpeningHoursResponse buildOpeningHoursForDay(DayOfWeek day, List<Slot> slots) {
        if (slots.isEmpty()) {
            return new OpeningHoursResponse(capitalize(day), null, null);
        }

        LocalTime start = slots.stream()
                .map(slot -> slot.getHoursRange().getStart())
                .min(LocalTime::compareTo)
                .orElse(null);

        LocalTime end = slots.stream()
                .map(slot -> slot.getHoursRange().getEnd())
                .max(LocalTime::compareTo)
                .orElse(null);

        return new OpeningHoursResponse(capitalize(day), start, end);
    }

    private String capitalize(DayOfWeek dayOfWeek) {
        String name = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH);
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
