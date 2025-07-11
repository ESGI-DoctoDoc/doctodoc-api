package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetDoctorsToRecruitResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor_recruitment.IGetDoctorsToRecruit;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetDoctorsToRecruitController {
    private final IGetDoctorsToRecruit getDoctorsToRecruit;

    public GetDoctorsToRecruitController(IGetDoctorsToRecruit getDoctorsToRecruit) {
        this.getDoctorsToRecruit = getDoctorsToRecruit;
    }

    @GetMapping("admin/recruitments/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorsToRecruitResponse> getDoctorsToRecruit() {
        return this.getDoctorsToRecruit.get();
    }
}
