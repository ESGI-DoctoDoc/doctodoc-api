package fr.esgi.doctodocapi.use_cases.doctor.manage_payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import fr.esgi.doctodocapi.infrastructure.services.StripeService;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.SubscriptionAlreadyActiveException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class PayDoctorSubscription implements IPayDoctorSubscription {
    private final DoctorRepository doctorRepository;
    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;
    private final DoctorInvoiceRepository doctorInvoiceRepository;

    public PayDoctorSubscription(DoctorRepository doctorRepository, StripeService stripeService, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorInvoiceRepository doctorInvoiceRepository) {
        this.doctorRepository = doctorRepository;
        this.stripeService = stripeService;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
        this.doctorInvoiceRepository = doctorInvoiceRepository;
    }

    public SubscribeResponse execute(SubscribeRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Optional<DoctorSubscription> activeSubscription = this.doctorSubscriptionRepository.findActiveSubscriptionByDoctorId(doctor.getId());
            if (activeSubscription.isPresent()) {
                throw new SubscriptionAlreadyActiveException();
            }

            String customerId = this.stripeService.upsertCustomer(doctor);
            doctor.setStripeCustomerId(customerId);
            this.doctorRepository.save(doctor);

            String successUrl = request.successUrl();
            String cancelUrl = request.cancelUrl();
            Session session = this.stripeService.createCheckoutSession(customerId, successUrl, cancelUrl);

            DoctorSubscription subscription = DoctorSubscription.create(doctor.getId(), LocalDateTime.now());
            DoctorSubscription savedSubscription = this.doctorSubscriptionRepository.save(subscription);

            DoctorInvoice invoice = DoctorInvoice.create(savedSubscription.getId(), session.getId());
            this.doctorInvoiceRepository.save(invoice);

            return new SubscribeResponse(session.getUrl());
        } catch (StripeException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "payment_error", "Failed to process payment: " + e.getMessage());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public void confirm(String sessionId) {
        try {
            DoctorInvoice invoice = this.doctorInvoiceRepository.findBySessionId(sessionId);
            invoice.markAsPaid();

            BigDecimal amount = this.stripeService.getAmountPaid(sessionId);
            invoice.setAmount(amount);

            this.doctorInvoiceRepository.update(invoice);

            String subscriptionId = Session.retrieve(sessionId).getSubscription();
            this.stripeService.updateSubscriptionToCancelIt(subscriptionId);

        } catch (StripeException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "stripe_error", "Failed to confirm payment: " + e.getMessage());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
