package fr.esgi.doctodocapi.use_cases.admin.get_subscription;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorSubscriptionResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.InvoiceState;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetSubscriptionForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IGetDoctorSubscriptionsForAdmin;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GetDoctorSubscriptionsForAdmin implements IGetDoctorSubscriptionsForAdmin {
    private final DoctorSubscriptionRepository subscriptionRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSubscriptionResponseMapper mapper;
    private final DoctorInvoiceRepository invoiceRepository;

    public GetDoctorSubscriptionsForAdmin(
            DoctorSubscriptionRepository subscriptionRepository,
            DoctorRepository doctorRepository,
            DoctorSubscriptionResponseMapper mapper, DoctorInvoiceRepository invoiceRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<GetSubscriptionForAdminResponse> execute(int page, int size) {
        try {
            List<DoctorSubscription> subscriptions = subscriptionRepository.findAllWithPagination(page, size);

            return subscriptions.stream()
                    .map(subscription -> {
                        UUID doctorId = subscription.getDoctorId();
                        Doctor doctor = doctorRepository.getById(doctorId);
                        DoctorInvoice invoice = invoiceRepository.findBySubscriptionId(subscription.getId());

                        boolean isActive = !subscription.getEndDate().isBefore(LocalDateTime.now())
                                && invoice.getState().equals(InvoiceState.PAID);

                        String status = isActive ? "active" : "inactive";

                        return mapper.toAdminResponse(subscription, doctor, invoice, status);
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
