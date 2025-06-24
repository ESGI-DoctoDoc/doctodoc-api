package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.services.StripeService;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_payment.PayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorPaymentConfiguration {

    @Bean
    public IPayDoctorSubscription payDoctorSubscription(DoctorRepository doctorRepository, StripeService stripeService, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorInvoiceRepository doctorInvoiceRepository) {
        return new PayDoctorSubscription(doctorRepository, stripeService, userRepository, getCurrentUserContext, doctorSubscriptionRepository, doctorInvoiceRepository);
    }
}