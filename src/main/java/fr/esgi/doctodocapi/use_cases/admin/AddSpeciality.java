package fr.esgi.doctodocapi.use_cases.admin;

import fr.esgi.doctodocapi.dtos.requests.admin.AddSpecialityRequest;
import fr.esgi.doctodocapi.dtos.responses.admin.GetSpecialityResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Use case for adding a new medical speciality.
 * <p>
 * This service receives a request to create a new {@link Speciality},
 * persists it using the {@link SpecialityRepository}, and returns a DTO response.
 */
@Service
public class AddSpeciality {
    private final SpecialityRepository specialityRepository;

    public AddSpeciality(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    /**
     * Executes the creation of a new medical speciality.
     * The name is provided by the request and the speciality is saved with the current date.
     *
     * @param addSpecialityRequest the request containing the name of the speciality to be created
     * @return a {@link GetSpecialityResponse} representing the newly saved speciality
     * @throws ApiException if a domain validation fails
     */
    public GetSpecialityResponse execute(AddSpecialityRequest addSpecialityRequest) {
        try {
            Speciality speciality = Speciality.create(addSpecialityRequest.name());
            Speciality savedSpeciality = this.specialityRepository.save(speciality);
            return new GetSpecialityResponse(
                    savedSpeciality.getId(),
                    savedSpeciality.getName(),
                    savedSpeciality.getCreatedAt()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
