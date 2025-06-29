package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetInvoiceUrlResponse;

import java.util.UUID;

public interface IFetchInvoiceForSubscription {
    GetInvoiceUrlResponse execute(UUID subscriptionId);
}
