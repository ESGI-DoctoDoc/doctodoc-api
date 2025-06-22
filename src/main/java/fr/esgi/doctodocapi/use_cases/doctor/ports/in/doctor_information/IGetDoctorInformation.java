package fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response.DoctorInfoResponse;

public interface IGetDoctorInformation {
    DoctorInfoResponse execute();
}
