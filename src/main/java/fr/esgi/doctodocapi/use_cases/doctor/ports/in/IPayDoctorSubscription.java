package fr.esgi.doctodocapi.use_cases.doctor.ports.in;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.SubscribeResponse;

public interface IPayDoctorSubscription {
    SubscribeResponse execute(SubscribeRequest request);
    void confirm(String sessionId);
}
