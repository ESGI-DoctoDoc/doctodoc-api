package fr.esgi.doctodocapi.infrastructure.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.PaymentProcessFailedException;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.PaymentProcess;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.LineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class StripeService implements PaymentProcess {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public String upsertCustomer(Doctor doctor) {
        try {
            if (doctor.getCustomerId() != null) return doctor.getCustomerId();

            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setName(doctor.getPersonalInformations().getFirstName() + " " + doctor.getPersonalInformations().getLastName())
                    .setEmail(doctor.getEmail().getValue())
                    .build();

            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (StripeException e) {
            throw new PaymentProcessFailedException();
        }
    }

    public Session createCheckoutSession(String customerId, String successUrl, String cancelUrl) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomer(customerId)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}&success=true")
                    .setCancelUrl(cancelUrl + "?session_id={CHECKOUT_SESSION_ID}&success=false")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPrice("price_1Rdby1Gf6jLO38qnlSkiEMfV")
                            .build())
                    .build();
            return Session.create(params);
        } catch (StripeException e) {
            throw new PaymentProcessFailedException();
        }
    }

    public String getSubscriptionIdFromSession(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return session.getSubscription();
        } catch (StripeException e) {
            throw new PaymentProcessFailedException();
        }
    }

    public void updateSubscriptionToCancelIt(String subscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();
            subscription.update(params);
        } catch (StripeException e) {
            throw new PaymentProcessFailedException();
        }
    }


    public BigDecimal getAmountPaid(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            StripeCollection<LineItem> lineItems = session.listLineItems();

            LineItem item = lineItems.getData().getFirst();
            long amountTotalInCents = item.getAmountTotal();

            return BigDecimal
                    .valueOf(amountTotalInCents)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (StripeException e) {
            throw new PaymentProcessFailedException();
        }
    }
}