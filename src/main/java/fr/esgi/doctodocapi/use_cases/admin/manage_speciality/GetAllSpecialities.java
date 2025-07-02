package fr.esgi.doctodocapi.use_cases.admin.manage_speciality;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.manage_speciality.IGetAllSpecialities;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Use case for retrieving all available medical specialities.
 * <p>
 * This service interacts with the {@link SpecialityRepository} to fetch all stored {@link Speciality} domain objects
 * and maps them to response DTOs for external usage.
 */
public class GetAllSpecialities implements IGetAllSpecialities {
    private final SpecialityRepository specialityRepository;

    public GetAllSpecialities(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }


    /**
     * Retrieves all stored medical specialities and maps them to {@link GetSpecialityResponse} DTOs.
     *
     * @return a list of {@link GetSpecialityResponse} representing all available specialities
     * @throws ApiException if a domain exception occurs during retrieval
     */
    public List<GetSpecialityResponse> getAll(int page, int size) {
        try {
            List<Speciality> specialities = this.specialityRepository.findAll(page, size);
            return specialities.stream().map(speciality -> new GetSpecialityResponse(
                    speciality.getId(),
                    speciality.getName(),
                    speciality.getCreatedAt()
            )).toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
