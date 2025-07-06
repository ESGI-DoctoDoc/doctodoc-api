package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorSubscriptionResponse;

import java.util.List;

public interface IGetDoctorSubscription {
    List<GetDoctorSubscriptionResponse> execute(int page, int size);
}
