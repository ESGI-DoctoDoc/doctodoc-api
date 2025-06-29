package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import org.springframework.stereotype.Service;

@Service
public class DoctorSubscriptionMapper {

    public DoctorSubscription toDomain(DoctorSubscriptionEntity entity) {
        return new DoctorSubscription(
                entity.getId(),
                entity.getDoctor().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt()
        );
    }

    public DoctorSubscriptionEntity toEntity(DoctorSubscription domain, DoctorEntity doctor) {
        DoctorSubscriptionEntity entity = new DoctorSubscriptionEntity();

        entity.setDoctor(doctor);
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
