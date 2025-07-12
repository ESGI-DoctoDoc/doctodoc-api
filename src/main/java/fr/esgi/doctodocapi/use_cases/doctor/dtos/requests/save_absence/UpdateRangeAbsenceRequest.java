package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateRangeAbsenceRequest(
        String description,
        LocalDate start,
        LocalDate end,
        LocalTime startHour,
        LocalTime endHour
) {}
