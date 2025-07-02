package fr.esgi.doctodocapi.use_cases.admin.manage_speciality;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.requests.UpdateSpecialityRequest;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality.IUpdateSpeciality;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdateSpeciality implements IUpdateSpeciality {
    private final SpecialityRepository specialityRepository;

    public UpdateSpeciality(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    public GetSpecialityResponse update(UUID id, UpdateSpecialityRequest request) {
        try {
            Speciality speciality = this.specialityRepository.findById(id);
            speciality.setName(request.name());
            Speciality updated = this.specialityRepository.save(speciality);
            return new GetSpecialityResponse(updated.getId(), updated.getName(), updated.getCreatedAt());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
