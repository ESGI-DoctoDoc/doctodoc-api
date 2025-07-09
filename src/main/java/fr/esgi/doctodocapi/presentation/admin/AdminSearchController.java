package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSubscriptionResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.search.IAdminSearchFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminSearchController {
    private final IAdminSearchFetcher adminSearchFetcher;

    public AdminSearchController(IAdminSearchFetcher adminSearchFetcher) {
        this.adminSearchFetcher = adminSearchFetcher;
    }

    @GetMapping("/search/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSearchDoctorForAdminResponse> getDoctorsByName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminSearchFetcher.searchDoctors(name, page, size);
    }

    @GetMapping("/search/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSearchAppointmentResponse> getAppointmentsByPatientName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminSearchFetcher.searchAppointments(name, page, size);
    }

    @GetMapping("/search/specialities")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSearchSpecialityResponse> getSpecialtiesByName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminSearchFetcher.searchSpecialities(name, page, size);
    }

    @GetMapping("/search/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetSearchSubscriptionResponse> getSubscriptionsByDoctorName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminSearchFetcher.searchSubscriptions(name, page, size);
    }
}
