package fr.esgi.doctodocapi.presentation.doctor.search;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.IDoctorSearchFetcher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors/search")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class DoctorSearchController {

    private final IDoctorSearchFetcher doctorSearchFetcher;

    public DoctorSearchController(IDoctorSearchFetcher doctorSearchFetcher) {
        this.doctorSearchFetcher = doctorSearchFetcher;
    }

    @GetMapping("/care-tracking")
    public List<GetDoctorCareTrackingResponse> searchCareTrackingByPatientName(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10000") int size
    ) {
        return doctorSearchFetcher.searchCareTracking(name, page, size);
    }

    @GetMapping("/medical-concerns")
    public List<GetDoctorMedicalConcernResponse> searchMedicalConcerns(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10000") int size
    ) {
        return doctorSearchFetcher.searchMedicalConcerns(name, page, size);
    }

    @GetMapping("/patients")
    public List<GetDoctorPatientResponse> searchPatients(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10000") int size
    ) {
        return doctorSearchFetcher.searchPatients(name, page, size);
    }


    @GetMapping("/appointments")
    public List<GetSearchAppointmentResponse> searchAppointments(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10000") int size
    ) {
        return doctorSearchFetcher.searchAppointments(name, page, size);
    }
}
