package fr.esgi.doctodocapi.presentation.doctor.manage_subscription;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.SubscribeRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorInvoiceUrlResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.GetDoctorSubscriptionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response.SubscribeResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IPayDoctorSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IFetchDoctorInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_subscription.IGetDoctorSubscription;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageDoctorSubscriptionController {
    private final IPayDoctorSubscription payDoctorSubscription;
    private final IGetDoctorSubscription getDoctorSubscription;
    private final IFetchDoctorInvoiceForSubscription fetchDoctorInvoiceForSubscription;

    public ManageDoctorSubscriptionController(IPayDoctorSubscription payDoctorSubscription, IGetDoctorSubscription getDoctorSubscription, IFetchDoctorInvoiceForSubscription fetchDoctorInvoiceForSubscription) {
        this.payDoctorSubscription = payDoctorSubscription;
        this.getDoctorSubscription = getDoctorSubscription;
        this.fetchDoctorInvoiceForSubscription = fetchDoctorInvoiceForSubscription;
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

    @GetMapping("subscriptions/{subscriptionId}/invoice")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorInvoiceUrlResponse getInvoiceUrl(@PathVariable UUID subscriptionId) {
        return this.fetchDoctorInvoiceForSubscription.execute(subscriptionId);
    }
}
