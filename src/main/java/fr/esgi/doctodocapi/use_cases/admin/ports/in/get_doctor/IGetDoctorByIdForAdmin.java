package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorByIdResponse;

import java.util.UUID;

public interface IGetDoctorByIdForAdmin {
    GetDoctorByIdResponse execute(UUID doctorId);
}
