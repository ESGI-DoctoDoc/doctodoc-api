package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalConcernMapper {
    public MedicalConcern toDomain(MedicalConcernEntity entity) {
        return new MedicalConcern(
                entity.getId(),
                entity.getName(),
                entity.getDurationInMinutes(),
                List.of(),
                entity.getPrice(),
                entity.getDoctor().getId(),
                entity.getCreatedAt()
        );
    }

    public MedicalConcern toDomain(MedicalConcernEntity entity, List<Question> questions) {
        return new MedicalConcern(
                entity.getId(),
                entity.getName(),
                entity.getDurationInMinutes(),
                questions,
                entity.getPrice(),
                entity.getDoctor().getId(),
                entity.getCreatedAt()
        );
    }

    public MedicalConcernEntity toEntity(MedicalConcern medicalConcern, DoctorEntity doctor) {
        MedicalConcernEntity entity = new MedicalConcernEntity();
        entity.setDoctor(doctor);
        entity.setName(medicalConcern.getName());
        entity.setDurationInMinutes(medicalConcern.getDurationInMinutes().getValue());
        entity.setPrice(medicalConcern.getPrice());
        entity.setCreatedAt(medicalConcern.getCreatedAt());
        return entity;
    }
}
