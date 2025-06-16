package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.dtos.responses.doctor.absence.GetAbsenceResponse;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRange;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class AbsenceResponseMapper {
    private static final DateTimeFormatter HOUR_MIN_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<GetAbsenceResponse> toResponseList(List<Absence> absences) {
        return absences.stream()
                .map(this::toResponse)
                .toList();
    }

    public GetAbsenceResponse toResponse(Absence absence) {
        AbsenceRange range = absence.getAbsenceRange();
        LocalDate date = null;

        if (range != null && range.getDateRange() != null && Objects.equals(range.getDateRange().getStart(), range.getDateRange().getEnd())) {
            date = range.getDateRange().getStart();
        }

        return new GetAbsenceResponse(
                absence.getId(),
                absence.getDescription(),
                date,
                range != null && range.getDateRange() != null ? range.getDateRange().getStart() : null,
                range != null && range.getDateRange() != null ? range.getDateRange().getEnd() : null,
                range != null && range.getHoursRange() != null ? formatTime(range.getHoursRange().getStart()) : null,
                range != null && range.getHoursRange() != null ? formatTime(range.getHoursRange().getEnd()) : null,
                absence.getCreatedAt()
        );
    }

    private String formatTime(LocalTime time) {
        return time.format(HOUR_MIN_FORMATTER);
    }
}