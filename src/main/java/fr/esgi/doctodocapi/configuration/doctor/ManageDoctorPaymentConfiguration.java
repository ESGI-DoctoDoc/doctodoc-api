package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionResponseMapper;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_subscription.GetDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.manage_subscription.PayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IGetDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.PaymentProcess;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorPaymentConfiguration {

    @Bean
    public IPayDoctorSubscription payDoctorSubscription(DoctorRepository doctorRepository, PaymentProcess paymentProcess, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorInvoiceRepository doctorInvoiceRepository) {
        return new PayDoctorSubscription(doctorRepository, userRepository, getCurrentUserContext, doctorSubscriptionRepository, doctorInvoiceRepository, paymentProcess);
    }

    @Bean
    public IGetDoctorSubscription getDoctorSubscription(DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorInvoiceRepository doctorInvoiceRepository, DoctorSubscriptionResponseMapper doctorSubscriptionResponseMapper) {
        return new GetDoctorSubscription(doctorSubscriptionRepository, doctorRepository, userRepository, getCurrentUserContext, doctorInvoiceRepository, doctorSubscriptionResponseMapper);
    }
}