package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
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

        if (range.getDateRange() != null && Objects.equals(range.getDateRange().getStart(), range.getDateRange().getEnd())) {
            date = range.getDateRange().getStart();
        }

        return new GetAbsenceResponse(
                absence.getId(),
                absence.getDescription(),
                date,
                range.getDateRange().getStart(),
                range.getDateRange().getEnd(),
                formatTime(range.getHoursRange().getStart()),
                formatTime(range.getHoursRange().getEnd()),
                absence.getCreatedAt()
        );
    }

    private String formatTime(LocalTime time) {
        return time.format(HOUR_MIN_FORMATTER);
    }
}