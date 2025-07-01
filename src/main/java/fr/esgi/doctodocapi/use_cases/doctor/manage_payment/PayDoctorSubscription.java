package fr.esgi.doctodocapi.use_cases.doctor.manage_payment;

import com.stripe.model.checkout.Session;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceState;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.PaymentProcess;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.SubscriptionAlreadyActiveException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class PayDoctorSubscription implements IPayDoctorSubscription {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;
    private final DoctorInvoiceRepository doctorInvoiceRepository;
    private final PaymentProcess paymentProcess;

    public PayDoctorSubscription(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorInvoiceRepository doctorInvoiceRepository, PaymentProcess paymentProcess) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
        this.doctorInvoiceRepository = doctorInvoiceRepository;
        this.paymentProcess = paymentProcess;
    }

    public SubscribeResponse execute(SubscribeRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Optional<DoctorSubscription> paidSubscription = this.doctorSubscriptionRepository.findActivePaidSubscriptionByDoctorId(doctor.getId());
            if (paidSubscription.isPresent()) {
                throw new SubscriptionAlreadyActiveException();
            }

            Optional<DoctorSubscription> latestSubscription = this.doctorSubscriptionRepository.findLatestSubscriptionByDoctorId(doctor.getId());
            return latestSubscription.map(doctorSubscription -> handleExistingSubscription(doctorSubscription, doctor, request)).orElseGet(() -> createNewSubscription(doctor, request));

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public void confirm(String sessionId) {
        try {
            DoctorInvoice invoice = this.doctorInvoiceRepository.findBySessionId(sessionId);
            invoice.markAsPaid();

            this.doctorInvoiceRepository.update(invoice);

            String subscriptionId = this.paymentProcess.getSubscriptionIdFromSession(sessionId);
            this.paymentProcess.updateSubscriptionToCancelIt(subscriptionId);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private SubscribeResponse createNewSubscription(Doctor doctor, SubscribeRequest request) {
        String customerId = this.paymentProcess.upsertCustomer(doctor);
        doctor.setCustomerId(customerId);
        this.doctorRepository.save(doctor);

        Session session = this.paymentProcess.createCheckoutSession(customerId, request.successUrl(), request.cancelUrl());

        BigDecimal amount = this.paymentProcess.getSubscriptionAmount(session.getId());

        DoctorSubscription subscription = DoctorSubscription.create(doctor.getId(), LocalDateTime.now());
        DoctorSubscription savedSubscription = this.doctorSubscriptionRepository.save(subscription);

        DoctorInvoice invoice = DoctorInvoice.create(savedSubscription.getId(), amount, session.getId());
        this.doctorInvoiceRepository.save(invoice);

        return new SubscribeResponse(session.getUrl());
    }

    private SubscribeResponse handleExistingSubscription(DoctorSubscription subscription, Doctor doctor, SubscribeRequest request) {
        DoctorInvoice invoice = this.doctorInvoiceRepository.findBySubscriptionId(subscription.getId());

        if (invoice.getState().equals(InvoiceState.UNPAID)) {
            String customerId = this.paymentProcess.upsertCustomer(doctor);
            doctor.setCustomerId(customerId);
            this.doctorRepository.save(doctor);

            Session session = this.paymentProcess.createCheckoutSession(customerId, request.successUrl(), request.cancelUrl());

            invoice.setSessionId(session.getId());
            this.doctorInvoiceRepository.update(invoice);

            return new SubscribeResponse(session.getUrl());
        } else {
            throw new SubscriptionAlreadyActiveException();
        }
    }
}
