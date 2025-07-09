package fr.esgi.doctodocapi.presentation.doctor.manage_subscription;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetSubscriptionForAdminResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorSubscriptionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IGetDoctorSubscription;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageDoctorSubscriptionController {
    private final IPayDoctorSubscription payDoctorSubscription;
    private final IGetDoctorSubscription getDoctorSubscription;

    public ManageDoctorSubscriptionController(IPayDoctorSubscription payDoctorSubscription, IGetDoctorSubscription getDoctorSubscription) {
        this.payDoctorSubscription = payDoctorSubscription;
        this.getDoctorSubscription = getDoctorSubscription;
    }

    @PostMapping("subscriptions")
    public SubscribeResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return this.payDoctorSubscription.execute(request);
    }

    @PostMapping("subscriptions/{sessionId}/confirm")
    public void confirmSubscription(@PathVariable String sessionId) {
        this.payDoctorSubscription.confirm(sessionId);
    }

    @GetMapping("subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorSubscriptionResponse> getSubscriptions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return this.getDoctorSubscription.execute(page, size);
    }
}
