package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.CreateMedicalConcernRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.ICreateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Use case for creating a new MedicalConcern (pour un doctor donné).
 * Si la requête contient des questions, on délègue leur création à AddQuestion.
 */
public class CreateMedicalConcern implements ICreateMedicalConcern {
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorRepository doctorRepository;

    public CreateMedicalConcern(MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Executes the creation of a new medical concern.
     * <p>
     *
     * @param createMedicalConcernRequest the request containing concern info
     * @return a {@link GetMedicalConcernResponse} representing the saved concern
     * @throws ApiException if any domain rule is violated or the user is invalid
     */
    public GetMedicalConcernResponse execute(CreateMedicalConcernRequest createMedicalConcernRequest) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            MedicalConcern newMedicalConcern = MedicalConcern.create(
                    createMedicalConcernRequest.name(),
                    createMedicalConcernRequest.duration(),
                    createMedicalConcernRequest.price(),
                    doctor.getId()
            );

            MedicalConcern savedConcern = this.medicalConcernRepository.save(newMedicalConcern);

            return new GetMedicalConcernResponse(
                    savedConcern.getId(),
                    savedConcern.getName(),
                    savedConcern.getDurationInMinutes().getValue(),
                    savedConcern.getPrice(),
                    doctor.getId(),
                    List.of(),
                    savedConcern.getCreatedAt()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
