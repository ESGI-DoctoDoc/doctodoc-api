package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoice;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscription;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.DoctorInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetSubscriptionForAdminResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DoctorSubscriptionResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetSubscriptionForAdminResponse toAdminResponse(DoctorSubscription subscription, Doctor doctor, DoctorInvoice invoice) {
        String status = computeStatus(subscription.getEndDate());

        return new GetSubscriptionForAdminResponse(
                subscription.getId(),
                new DoctorInfoForAdmin(
                        doctor.getId(),
                        doctor.getPersonalInformations().getFirstName(),
                        doctor.getPersonalInformations().getLastName(),
                        doctor.getEmail().getValue()
                ),
                subscription.getStartDate().toString(),
                subscription.getEndDate().toString(),
                invoice.getAmount(),
                status,
                formatDate(subscription.getCreatedAt())
        );
    }

    private String computeStatus(LocalDateTime endDate) {
        return endDate.isBefore(LocalDateTime.now()) ? "inactive" : "active";
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }
}
