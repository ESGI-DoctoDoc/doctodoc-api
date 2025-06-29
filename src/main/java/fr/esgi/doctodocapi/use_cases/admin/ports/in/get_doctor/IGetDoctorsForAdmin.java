package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorForAdminResponse;

import java.util.List;

public interface IGetDoctorsForAdmin {
    List<GetDoctorForAdminResponse> execute(int page, int size);
}
