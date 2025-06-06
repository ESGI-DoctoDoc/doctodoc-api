package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SpecialityEntity;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import org.springframework.stereotype.Service;

@Service
public class SpecialityMapper {
    public Speciality toDomain(SpecialityEntity entity) {
        return new Speciality(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt()
        );
    }

    public SpecialityEntity toEntity(Speciality speciality) {
        SpecialityEntity entity = new SpecialityEntity();
        entity.setId(speciality.getId());
        entity.setName(speciality.getName());
        entity.setCreatedAt(speciality.getCreatedAt());
        return entity;
    }
}
