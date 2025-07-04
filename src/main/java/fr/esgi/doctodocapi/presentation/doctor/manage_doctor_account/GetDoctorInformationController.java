package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.DoctorInfoResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information.IGetDoctorInformation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("doctors")
public class GetDoctorInformationController {
    private final IGetDoctorInformation getDoctorInformation;

    public GetDoctorInformationController(IGetDoctorInformation getDoctorInformation) {
        this.getDoctorInformation = getDoctorInformation;
    }

    @GetMapping("auth/me")
    @ResponseStatus(HttpStatus.OK)
    public DoctorInfoResponse getDoctorInfo() {
        return getDoctorInformation.execute();
    }
}
