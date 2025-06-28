package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorInvoiceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorInvoiceJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorInvoiceMapper;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DoctorInvoiceRepositoryImpl implements DoctorInvoiceRepository {
    private final DoctorInvoiceJpaRepository doctorInvoiceJpaRepository;
    private final EntityManager entityManager;
    private final DoctorInvoiceMapper doctorInvoiceMapper;

    public DoctorInvoiceRepositoryImpl(DoctorInvoiceJpaRepository doctorInvoiceJpaRepository, EntityManager entityManager, DoctorInvoiceMapper doctorInvoiceMapper) {
        this.doctorInvoiceJpaRepository = doctorInvoiceJpaRepository;
        this.entityManager = entityManager;
        this.doctorInvoiceMapper = doctorInvoiceMapper;
    }

    @Override
    public void save(DoctorInvoice invoice) {
        DoctorSubscriptionEntity doctorSubscriptionEntity = this.entityManager.getReference(DoctorSubscriptionEntity.class, invoice.getSubscription());
        DoctorInvoiceEntity doctorInvoiceEntity = this.doctorInvoiceMapper.toEntity(invoice, doctorSubscriptionEntity);
        this.doctorInvoiceJpaRepository.save(doctorInvoiceEntity);
    }

    @Override
    public DoctorInvoice findBySessionId(String sessionId) {
        DoctorInvoiceEntity entity = this.doctorInvoiceJpaRepository.findBySessionId(sessionId);
        return this.doctorInvoiceMapper.toDomain(entity);
    }

    @Override
    public void update(DoctorInvoice invoice) {
        DoctorInvoiceEntity existingEntity = this.doctorInvoiceJpaRepository
                .findById(invoice.getId())
                .orElseThrow(InvoiceNotFoundException::new);

        existingEntity.setState(invoice.getState().name());
        existingEntity.setAmount(invoice.getAmount());
        existingEntity.setSessionId(invoice.getSessionId());

        this.doctorInvoiceJpaRepository.save(existingEntity);
    }

    @Override
    public DoctorInvoice findBySubscriptionId(UUID subscriptionId) {
        DoctorInvoiceEntity entity = this.doctorInvoiceJpaRepository.findBySubscription_Id(subscriptionId);
        return this.doctorInvoiceMapper.toDomain(entity);
    }
}
