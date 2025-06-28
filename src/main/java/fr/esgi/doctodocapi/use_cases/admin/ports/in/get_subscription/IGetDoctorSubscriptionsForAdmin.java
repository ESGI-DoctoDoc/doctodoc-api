package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetSubscriptionForAdminResponse;

import java.util.List;

public interface IGetDoctorSubscriptionsForAdmin {
    List<GetSubscriptionForAdminResponse> execute(int page, int size);
}
