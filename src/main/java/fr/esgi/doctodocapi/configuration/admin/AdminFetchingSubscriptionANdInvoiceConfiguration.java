package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionResponseMapper;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.get_subscription.FetchInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.admin.get_subscription.GetDoctorSubscriptionsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IFetchInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IGetDoctorSubscriptionsForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.InvoiceFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminFetchingSubscriptionANdInvoiceConfiguration {

    @Bean
    public IGetDoctorSubscriptionsForAdmin getDoctorSubscriptionsForAdmin(DoctorSubscriptionRepository subscriptionRepository, DoctorRepository doctorRepository, DoctorInvoiceRepository invoiceRepository, DoctorSubscriptionResponseMapper mapper) {
        return new GetDoctorSubscriptionsForAdmin(subscriptionRepository, doctorRepository, mapper, invoiceRepository);
    }

    @Bean
    public IFetchInvoiceForSubscription getInvoiceForSubscription(DoctorInvoiceRepository invoiceRepository, InvoiceFetcher invoiceFetcher) {
        return new FetchInvoiceForSubscription(invoiceRepository, invoiceFetcher);
    }
}
