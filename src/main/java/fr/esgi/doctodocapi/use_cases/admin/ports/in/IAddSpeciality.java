package fr.esgi.doctodocapi.use_cases.admin.ports.in;

import fr.esgi.doctodocapi.use_cases.admin.dtos.requests.AddSpecialityRequest;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;

public interface IAddSpeciality {
    GetSpecialityResponse execute(AddSpecialityRequest request);
}
