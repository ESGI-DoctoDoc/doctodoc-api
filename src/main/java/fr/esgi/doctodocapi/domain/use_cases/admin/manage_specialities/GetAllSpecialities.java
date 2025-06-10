package fr.esgi.doctodocapi.domain.use_cases.admin.manage_specialities;

import fr.esgi.doctodocapi.dtos.responses.admin.GetSpecialityResponse;
import fr.esgi.doctodocapi.domain.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.domain.DomainException;
import fr.esgi.doctodocapi.domain.entities.admin.speciality.Speciality;
import fr.esgi.doctodocapi.domain.entities.admin.speciality.SpecialityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all available medical specialities.
 * <p>
 * This service interacts with the {@link SpecialityRepository} to fetch all stored {@link Speciality} domain objects
 * and maps them to response DTOs for external usage.
 */
@Service
public class GetAllSpecialities {
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
    public List<GetSpecialityResponse> getAll() {
        try {
            List<Speciality> specialities = this.specialityRepository.findAll();
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
