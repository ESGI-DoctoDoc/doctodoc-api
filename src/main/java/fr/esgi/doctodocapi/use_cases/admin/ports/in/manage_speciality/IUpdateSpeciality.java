package fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality;

import fr.esgi.doctodocapi.use_cases.admin.dtos.requests.UpdateSpecialityRequest;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;

import java.util.UUID;

public interface IUpdateSpeciality {
    GetSpecialityResponse update(UUID id, UpdateSpecialityRequest request);
}
