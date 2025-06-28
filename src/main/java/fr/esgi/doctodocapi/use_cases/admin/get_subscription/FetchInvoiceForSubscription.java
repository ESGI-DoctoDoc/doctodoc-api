package fr.esgi.doctodocapi.use_cases.admin.get_subscription;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetInvoiceUrlResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IFetchInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.InvoiceFetcher;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class FetchInvoiceForSubscription implements IFetchInvoiceForSubscription {

    private final DoctorInvoiceRepository invoiceRepository;
    private final InvoiceFetcher invoiceFetcher;

    public FetchInvoiceForSubscription(DoctorInvoiceRepository invoiceRepository, InvoiceFetcher invoiceFetcher) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceFetcher = invoiceFetcher;
    }

    public GetInvoiceUrlResponse execute(UUID subscriptionId) {
        try {
            DoctorInvoice invoice = this.invoiceRepository.findBySubscriptionId(subscriptionId);
            String url = this.invoiceFetcher.getInvoiceUrl(invoice.getSessionId());
            return new GetInvoiceUrlResponse(url);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}