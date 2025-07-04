package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetDoctorProfileInformationResponse;

public interface IGetDoctorProfileInformation {
    GetDoctorProfileInformationResponse execute();
}
