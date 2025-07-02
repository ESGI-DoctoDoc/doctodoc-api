package fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;

import java.util.List;

public interface IGetAllSpecialities {
    List<GetSpecialityResponse> getAll(int page, int size);
}
