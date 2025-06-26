package fr.esgi.doctodocapi.use_cases.doctor.ports.out;

import com.stripe.model.checkout.Session;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.PaymentProcessFailedException;

import java.math.BigDecimal;

public interface PaymentProcess {
    String upsertCustomer(Doctor doctor) throws PaymentProcessFailedException;
    Session createCheckoutSession(String customerId, String successUrl, String cancelUrl) throws PaymentProcessFailedException;
    void updateSubscriptionToCancelIt(String subscriptionId) throws PaymentProcessFailedException;
    BigDecimal getAmountPaid(String sessionId) throws PaymentProcessFailedException;
    String getSubscriptionIdFromSession(String sessionId) throws PaymentProcessFailedException;
}
