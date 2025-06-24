package fr.esgi.doctodocapi.presentation.doctor.manage_payment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors")
public class ManageDoctorPaymentController {
    private final IPayDoctorSubscription payDoctorSubscription;

    public ManageDoctorPaymentController(IPayDoctorSubscription payDoctorSubscription) {
        this.payDoctorSubscription = payDoctorSubscription;
    }

    @PostMapping("subscriptions")
    public SubscribeResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return this.payDoctorSubscription.execute(request);
    }

    @PostMapping("subscriptions/{sessionId}/confirm")
    public void confirmSubscription(@PathVariable String sessionId) {
        this.payDoctorSubscription.confirm(sessionId);
    }
}
