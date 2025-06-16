package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRange;
import org.springframework.stereotype.Service;

@Service
public class AbsenceMapper {

    public Absence toDomain(AbsenceEntity entity) {
        AbsenceRange absenceRange = AbsenceRange.of(
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStartHour(),
                entity.getEndHour()
        );

        return new Absence(
                entity.getId(),
                entity.getDescription(),
                absenceRange,
                entity.getCreatedAt()
        );
    }

    public AbsenceEntity toEntity(Absence domain, DoctorEntity doctor) {
        AbsenceEntity absenceEntity = new AbsenceEntity();
        absenceEntity.setId(domain.getId());
        absenceEntity.setDescription(domain.getDescription());
        absenceEntity.setCreatedAt(domain.getCreatedAt());
        absenceEntity.setDoctor(doctor);

        AbsenceRange absenceRange = domain.getAbsenceRange();
        absenceEntity.setStartDate(absenceRange.getDateRange().getStart());
        absenceEntity.setEndDate(absenceRange.getDateRange().getEnd());
        absenceEntity.setStartHour(absenceRange.getHoursRange().getStart());
        absenceEntity.setEndHour(absenceRange.getHoursRange().getEnd());

        return absenceEntity;
    }
}