package fr.esgi.doctodocapi.use_cases.admin.ports.in;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;

import java.util.List;

public interface IGetAllSpecialities {
    List<GetSpecialityResponse> getAll();
}
