package fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality;

import fr.esgi.doctodocapi.use_cases.admin.dtos.requests.AddSpecialityRequest;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;

public interface IAddSpeciality {
    GetSpecialityResponse execute(AddSpecialityRequest request);
}
