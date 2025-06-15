package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRange;
import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import org.springframework.stereotype.Service;

@Service
public class AbsenceMapper {

    public Absence toDomain(AbsenceEntity entity) {
        HoursRange hoursRange = entity.getStartHour() != null && entity.getEndHour() != null
                ? HoursRange.of(entity.getStartHour(), entity.getEndHour())
                : null;
        DateRange dateRange = entity.getStartDate() != null && entity.getEndDate() != null
                ? DateRange.of(entity.getStartDate(), entity.getEndDate())
                : null;

        AbsenceRange absenceRange = (hoursRange != null && dateRange != null)
                ? new AbsenceRange(hoursRange, dateRange)
                : null;

        return new Absence(
                entity.getId(),
                entity.getDescription(),
                entity.getDate(),
                absenceRange,
                entity.getCreatedAt()
        );
    }

    public AbsenceEntity toEntity(Absence domain, DoctorEntity doctor) {
        AbsenceEntity absenceEntity = new AbsenceEntity();
        absenceEntity.setId(domain.getId());
        absenceEntity.setDescription(domain.getDescription());
        absenceEntity.setDate(domain.getDate());
        absenceEntity.setCreatedAt(domain.getCreatedAt());
        absenceEntity.setDoctor(doctor);

        if (domain.getAbsenceRange() == null) {
            absenceEntity.setStartDate(null);
            absenceEntity.setEndDate(null);
            absenceEntity.setStartHour(null);
            absenceEntity.setEndHour(null);
        }
        else {
            AbsenceRange absenceRange = domain.getAbsenceRange();
            absenceEntity.setStartDate(absenceRange.getDateRange().getStart());
            absenceEntity.setEndDate(absenceRange.getDateRange().getEnd());
            absenceEntity.setStartHour(absenceRange.getHoursRange().getStart());
            absenceEntity.setEndHour(absenceRange.getHoursRange().getEnd());
        }

        return absenceEntity;
    }
}