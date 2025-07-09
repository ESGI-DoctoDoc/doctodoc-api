package fr.esgi.doctodocapi.use_cases.doctor.ports.in;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.SubscribeResponse;

public interface IPayDoctorSubscription {
    SubscribeResponse execute(SubscribeRequest request);
    void confirm(String sessionId);
}
