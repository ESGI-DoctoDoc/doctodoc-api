package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetInvoiceUrlResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions.GetSubscriptionForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IFetchInvoiceForSubscription;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_subscription.IGetDoctorSubscriptionsForAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetDoctorSubscriptionController {
    private final IGetDoctorSubscriptionsForAdmin getDoctorSubscriptionsForAdmin;
    private final IFetchInvoiceForSubscription fetchInvoiceForSubscription;

    public GetDoctorSubscriptionController(IGetDoctorSubscriptionsForAdmin getDoctorSubscriptionsForAdmin, IFetchInvoiceForSubscription fetchInvoiceForSubscription) {
        this.getDoctorSubscriptionsForAdmin = getDoctorSubscriptionsForAdmin;
        this.fetchInvoiceForSubscription = fetchInvoiceForSubscription;
    }

    @GetMapping("subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSubscriptionForAdminResponse> getSubscriptions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return this.getDoctorSubscriptionsForAdmin.execute(page, size);
    }

    @GetMapping("subscriptions/{subscriptionId}/invoice")
    @ResponseStatus(HttpStatus.OK)
    public GetInvoiceUrlResponse getInvoiceUrl(@PathVariable UUID subscriptionId) {
        return this.fetchInvoiceForSubscription.execute(subscriptionId);
    }
}
