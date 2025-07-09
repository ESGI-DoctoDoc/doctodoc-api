package fr.esgi.doctodocapi.use_cases.doctor.manage_subscription;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionResponseMapper;
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
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorSubscriptionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IGetDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GetDoctorSubscription implements IGetDoctorSubscription {
    private final DoctorSubscriptionRepository doctorSubscriptionRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorInvoiceRepository doctorInvoiceRepository;
    private final DoctorSubscriptionResponseMapper doctorSubscriptionResponseMapper;

    public GetDoctorSubscription(DoctorSubscriptionRepository doctorSubscriptionRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorInvoiceRepository doctorInvoiceRepository, DoctorSubscriptionResponseMapper doctorSubscriptionResponseMapper) {
        this.doctorSubscriptionRepository = doctorSubscriptionRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorInvoiceRepository = doctorInvoiceRepository;
        this.doctorSubscriptionResponseMapper = doctorSubscriptionResponseMapper;
    }

    public List<GetDoctorSubscriptionResponse> execute(int page, int size) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<DoctorSubscription> subscriptions = this.doctorSubscriptionRepository.findAllByDoctorIdWithPagination(doctor.getId(), page, size);

            return subscriptions.stream()
                    .map(subscription -> {
                        DoctorInvoice invoice = this.doctorInvoiceRepository.findBySubscriptionId(subscription.getId());

                        String status;

                        if (invoice.getState().equals(InvoiceState.PAID)) {
                            status = subscription.getEndDate().isAfter(LocalDateTime.now()) ? "active" : "expired";
                        } else if (invoice.getState().equals(InvoiceState.PAYMENT_ERROR)) {
                            status = "payment_error";
                        } else {
                            status = "inactive";
                        }

                        return doctorSubscriptionResponseMapper.toDoctorResponse(subscription, doctor, invoice, status);
                    })
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
