package fr.esgi.doctodocapi.presentation.patient;

import fr.esgi.doctodocapi.dtos.requests.patient.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.dtos.requests.patient.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.domain.use_cases.patient.manage_patient_account.OnBoardingPatient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller handling patient onboarding and doctor recruitment requests.
 * Access restricted to authenticated users with 'PATIENT' role.
 */
@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class OnBoardingPatientController {
    private final OnBoardingPatient onBoardingPatient;

    public OnBoardingPatientController(OnBoardingPatient onBoardingPatient) {
        this.onBoardingPatient = onBoardingPatient;
    }

    /**
     * Processes patient onboarding data and returns basic patient information.
     *
     * @param patientOnBoardingRequest the onboarding data submitted by the patient
     * @return basic information of the onboarded patient
     */
    @PostMapping("patients/on-boarding")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GetProfileResponse onBoardingPatient(@Valid @RequestBody PatientOnBoardingRequest patientOnBoardingRequest) {
        return this.onBoardingPatient.process(patientOnBoardingRequest);
    }

    /**
     * Saves a doctor recruitment request submitted by the patient.
     *
     * @param saveDoctorRecruitmentRequest the doctor recruitment data
     */
    @PostMapping("doctor-recruitment")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveDoctorRecruitment(@Valid @RequestBody SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest) {
        this.onBoardingPatient.addDoctorForRecruitment(saveDoctorRecruitmentRequest);
    }
}
