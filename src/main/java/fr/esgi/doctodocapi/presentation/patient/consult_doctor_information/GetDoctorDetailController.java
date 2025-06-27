package fr.esgi.doctodocapi.presentation.patient.consult_doctor_information;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.IGetDoctorDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetDoctorDetailController {
    private final IGetDoctorDetail getDoctorDetail;

    public GetDoctorDetailController(IGetDoctorDetail getDoctorDetail) {
        this.getDoctorDetail = getDoctorDetail;
    }

    @GetMapping("/doctors/{id}")
    public GetDoctorDetailResponse get(@PathVariable UUID id) {
        return this.getDoctorDetail.get(id);
    }
}
