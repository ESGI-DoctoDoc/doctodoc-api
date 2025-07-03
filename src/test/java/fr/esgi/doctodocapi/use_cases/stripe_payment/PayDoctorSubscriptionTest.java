package fr.esgi.doctodocapi.use_cases.stripe_payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.PaymentProcessFailedException;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceState;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.manage_payment.PayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.PaymentProcess;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayDoctorSubscriptionTest {

    @Mock private DoctorRepository doctorRepository;
    @Mock private UserRepository userRepository;
    @Mock private GetCurrentUserContext getCurrentUserContext;
    @Mock private DoctorSubscriptionRepository subscriptionRepository;
    @Mock private DoctorInvoiceRepository invoiceRepository;
    @Mock private PaymentProcess paymentProcess;

    @InjectMocks
    private PayDoctorSubscription payDoctorSubscription;

    private final UUID doctorId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final String email = "doc@example.com";
    private User user;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(doctorId);
    }

    @Test
    void should_create_invoice_with_payment_error_when_stripe_fails() {
        // Given
        when(getCurrentUserContext.getUsername()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(doctorRepository.findDoctorByUserId(userId)).thenReturn(doctor);

        when(subscriptionRepository.findActivePaidSubscriptionByDoctorId(doctorId))
                .thenReturn(Optional.empty());
        when(subscriptionRepository.findLatestSubscriptionByDoctorId(doctorId))
                .thenReturn(Optional.empty());

        DoctorSubscription fakeSubscription = DoctorSubscription.create(doctorId, LocalDateTime.now());
        when(subscriptionRepository.save(any())).thenReturn(fakeSubscription);

        when(paymentProcess.upsertCustomer(any())).thenReturn("cus_test");
        doThrow(new PaymentProcessFailedException()).when(paymentProcess).createCheckoutSession(any(), any(), any());

        SubscribeRequest request = new SubscribeRequest("https://success", "https://cancel");

        // When + Then
        assertThatThrownBy(() -> payDoctorSubscription.execute(request))
                .isInstanceOf(RuntimeException.class);

        verify(invoiceRepository).save(argThat(invoice ->
                invoice.getSubscription().equals(fakeSubscription.getId()) &&
                        invoice.getState() == InvoiceState.PAYMENT_ERROR
        ));
    }

    @Test
    void should_create_invoice_when_payment_succeeds() {
        // Given
        when(getCurrentUserContext.getUsername()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(doctorRepository.findDoctorByUserId(userId)).thenReturn(doctor);

        when(subscriptionRepository.findActivePaidSubscriptionByDoctorId(doctorId))
                .thenReturn(Optional.empty());
        when(subscriptionRepository.findLatestSubscriptionByDoctorId(doctorId))
                .thenReturn(Optional.empty());

        DoctorSubscription subscription = DoctorSubscription.create(doctorId, LocalDateTime.now());
        when(subscriptionRepository.save(any())).thenReturn(subscription);
        when(paymentProcess.upsertCustomer(any())).thenReturn("cus_test");

        Session session = mock(Session.class);
        when(session.getId()).thenReturn("sess_test");
        when(session.getUrl()).thenReturn("https://checkout.url");
        when(paymentProcess.createCheckoutSession(any(), any(), any())).thenReturn(session);
        when(paymentProcess.getSubscriptionAmount("sess_test")).thenReturn(BigDecimal.TEN);

        SubscribeRequest request = new SubscribeRequest("https://success", "https://cancel");

        // When
        SubscribeResponse response = payDoctorSubscription.execute(request);

        // Then
        assertThat(response.redirectUrl()).isEqualTo("https://checkout.url");
        verify(invoiceRepository).save(argThat(invoice ->
                invoice.getSubscription().equals(subscription.getId()) &&
                        invoice.getAmount().equals(BigDecimal.TEN) &&
                        invoice.getSessionId().equals("sess_test") &&
                        invoice.getState() == InvoiceState.UNPAID
        ));
    }
}
