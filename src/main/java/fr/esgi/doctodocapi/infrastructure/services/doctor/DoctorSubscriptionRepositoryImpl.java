package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorSubscriptionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionMapper;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    public Optional<DoctorSubscription> findActivePaidSubscriptionByDoctorId(UUID doctorId) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, doctorId);
        return this.subscriptionJpaRepository
                .findFirstPaidSubscriptionByDoctor(doctorEntity, LocalDateTime.now())
                .map(this.doctorSubscriptionMapper::toDomain);
    }

    @Override
    public List<DoctorSubscription> findAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorSubscriptionEntity> pageResult = this.subscriptionJpaRepository.findAll(pageable);
        return pageResult.getContent().stream()
                .map(doctorSubscriptionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DoctorSubscription> findLatestSubscriptionByDoctorId(UUID doctorId) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, doctorId);
        return this.subscriptionJpaRepository
                .findLatestSubscriptionByDoctor(doctorEntity)
                .map(this.doctorSubscriptionMapper::toDomain);
    }

    @Override
    public List<DoctorSubscription> findAllByDoctorId(UUID doctorId) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, doctorId);

        return this.subscriptionJpaRepository.findAllByDoctor(doctorEntity)
                .stream()
                .map(this.doctorSubscriptionMapper::toDomain)
                .toList();
    }
}
