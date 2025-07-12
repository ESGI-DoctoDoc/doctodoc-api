package fr.esgi.doctodocapi.use_cases.doctor.manage_subscription;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.InvoiceFetcher;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorInvoiceUrlResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IFetchDoctorInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class FetchDoctorInvoiceForSubscription implements IFetchDoctorInvoiceForSubscription {

    private final DoctorInvoiceRepository invoiceRepository;
    private final InvoiceFetcher invoiceFetcher;

    public FetchDoctorInvoiceForSubscription(DoctorInvoiceRepository invoiceRepository, InvoiceFetcher invoiceFetcher) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceFetcher = invoiceFetcher;
    }

    public GetDoctorInvoiceUrlResponse execute(UUID subscriptionId) {
        try {
            DoctorInvoice invoice = this.invoiceRepository.findBySubscriptionId(subscriptionId);
            String url = this.invoiceFetcher.getInvoiceUrl(invoice.getSessionId());
            return new GetDoctorInvoiceUrlResponse(url);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
