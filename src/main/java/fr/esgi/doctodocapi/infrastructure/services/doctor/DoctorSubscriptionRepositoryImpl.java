package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorSubscriptionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionMapper;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DoctorSubscriptionRepositoryImpl implements DoctorSubscriptionRepository {
    private final DoctorSubscriptionJpaRepository subscriptionJpaRepository;
    private final DoctorSubscriptionMapper doctorSubscriptionMapper;
    private final EntityManager entityManager;

    public DoctorSubscriptionRepositoryImpl(DoctorSubscriptionJpaRepository subscriptionJpaRepository, DoctorSubscriptionMapper doctorSubscriptionMapper, EntityManager entityManager) {
        this.subscriptionJpaRepository = subscriptionJpaRepository;
        this.doctorSubscriptionMapper = doctorSubscriptionMapper;
        this.entityManager = entityManager;
    }

    @Override
    public DoctorSubscription save(DoctorSubscription subscription) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, subscription.getDoctorId());
        DoctorSubscriptionEntity subscriptionEntity = this.doctorSubscriptionMapper.toEntity(subscription, doctorEntity);
        DoctorSubscriptionEntity savedEntity = this.subscriptionJpaRepository.save(subscriptionEntity);
        return this.doctorSubscriptionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DoctorSubscription> findActiveSubscriptionByDoctorId(UUID doctorId) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, doctorId);
        return this.subscriptionJpaRepository
                .findFirstByDoctorAndEndDateAfterOrderByEndDateDesc(doctorEntity, LocalDateTime.now())
                .map(this.doctorSubscriptionMapper::toDomain);
    }
}
