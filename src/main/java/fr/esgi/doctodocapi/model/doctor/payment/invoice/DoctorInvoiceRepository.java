package fr.esgi.doctodocapi.model.doctor.payment.invoice;

import java.util.UUID;

public interface DoctorInvoiceRepository {
    void save(DoctorInvoice invoice);
    DoctorInvoice findBySessionId(String sessionId) throws InvoiceNotFoundException;
    void update(DoctorInvoice invoice);
    DoctorInvoice findBySubscriptionId(UUID subscriptionId);
}
