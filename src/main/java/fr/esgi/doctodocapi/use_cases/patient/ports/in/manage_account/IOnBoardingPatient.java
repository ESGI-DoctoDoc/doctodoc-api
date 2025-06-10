package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account;

import fr.esgi.doctodocapi.dtos.requests.patient.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.dtos.requests.patient.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;

public interface IOnBoardingPatient {
    GetProfileResponse process(PatientOnBoardingRequest patientOnBoardingRequest);
    void addDoctorForRecruitment(SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest);
}
