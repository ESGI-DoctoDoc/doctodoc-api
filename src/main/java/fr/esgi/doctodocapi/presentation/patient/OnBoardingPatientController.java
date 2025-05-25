package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.patient.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.dtos.requests.patient.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.dtos.responses.GetBasicPatientInfo;
import fr.esgi.doctodocapi.use_cases.patient.OnBoardingPatient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class OnBoardingPatientController {
    private final OnBoardingPatient onBoardingPatient;

    public OnBoardingPatientController(OnBoardingPatient onBoardingPatient) {
        this.onBoardingPatient = onBoardingPatient;
    }

    @PostMapping("patients/on-boarding")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GetBasicPatientInfo onBoardingPatient(@Valid @RequestBody PatientOnBoardingRequest patientOnBoardingRequest) {
        return this.onBoardingPatient.process(patientOnBoardingRequest);
    }

    @PostMapping("doctor-recruitment")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveDoctorRecruitment(@Valid @RequestBody SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest) {
        this.onBoardingPatient.addDoctorForRecruitment(saveDoctorRecruitmentRequest);
    }
}
