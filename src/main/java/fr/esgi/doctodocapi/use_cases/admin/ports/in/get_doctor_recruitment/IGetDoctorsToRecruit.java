package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor_recruitment;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetDoctorsToRecruitResponse;

import java.util.List;

public interface IGetDoctorsToRecruit {
    List<GetDoctorsToRecruitResponse> get();
}
