package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.GetDoctorDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class GetDoctorDetailController {
    private final GetDoctorDetail getDoctorDetail;

    public GetDoctorDetailController(GetDoctorDetail getDoctorDetail) {
        this.getDoctorDetail = getDoctorDetail;
    }

    @GetMapping("/doctors/{id}")
    public GetDoctorDetailResponse get(@PathVariable UUID id) {
        return this.getDoctorDetail.get(id);
    }
}
