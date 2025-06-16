package fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;

import java.util.UUID;

public interface IGetDoctorDetail {
    GetDoctorDetailResponse get(UUID id);
}
