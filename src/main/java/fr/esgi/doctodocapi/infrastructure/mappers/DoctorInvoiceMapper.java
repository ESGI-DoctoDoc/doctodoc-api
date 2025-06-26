package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorInvoiceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceState;
import org.springframework.stereotype.Service;

@Service
public class DoctorInvoiceMapper {

    public DoctorInvoice toDomain(DoctorInvoiceEntity entity) {
        return new DoctorInvoice(
                entity.getId(),
                entity.getSubscription().getId(),
                InvoiceState.valueOf(entity.getState()),
                entity.getAmount(),
                entity.getSessionId()
        );
    }

    public DoctorInvoiceEntity toEntity(DoctorInvoice domain, DoctorSubscriptionEntity subscription) {
        DoctorInvoiceEntity entity = new DoctorInvoiceEntity();

        entity.setSubscription(subscription);
        entity.setState(domain.getState().name());
        entity.setAmount(domain.getAmount());
        entity.setSessionId(domain.getSessionId());
        return entity;
    }
}
