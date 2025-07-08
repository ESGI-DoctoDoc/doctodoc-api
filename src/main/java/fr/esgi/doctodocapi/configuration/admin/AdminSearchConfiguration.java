package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.search.IAdminSearchFetcher;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.RetrieveSearchData;
import fr.esgi.doctodocapi.use_cases.admin.search.AdminSearchFetcher;
import fr.esgi.doctodocapi.use_cases.admin.search.SearchAdminPresentationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSearchConfiguration {

    @Bean
    public IAdminSearchFetcher getAdminSearchFetcher(RetrieveSearchData retrieveSearchData, SearchAdminPresentationMapper searchAdminPresentationMapper, DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorInvoiceRepository doctorInvoiceRepository) {
        return new AdminSearchFetcher(retrieveSearchData, searchAdminPresentationMapper, doctorInvoiceRepository, doctorSubscriptionRepository);
    }
}
