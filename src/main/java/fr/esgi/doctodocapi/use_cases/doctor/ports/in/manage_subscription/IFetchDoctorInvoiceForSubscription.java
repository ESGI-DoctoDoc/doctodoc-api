package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorInvoiceUrlResponse;

import java.util.UUID;

public interface IFetchDoctorInvoiceForSubscription {
    GetDoctorInvoiceUrlResponse execute(UUID subscriptionId);
}
