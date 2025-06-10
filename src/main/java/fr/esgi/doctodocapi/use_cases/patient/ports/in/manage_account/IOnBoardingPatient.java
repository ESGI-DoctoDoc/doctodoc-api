package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.PatientOnBoardingRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDoctorRecruitmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;

public interface IOnBoardingPatient {
    GetProfileResponse process(PatientOnBoardingRequest patientOnBoardingRequest);
    void addDoctorForRecruitment(SaveDoctorRecruitmentRequest saveDoctorRecruitmentRequest);
}
