package fr.esgi.doctodocapi.domain.use_cases.doctor.manage_medical_concern;

import fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern.GetMedicalConcernResponse;
import fr.esgi.doctodocapi.dtos.responses.doctor.question.GetQuestionResponse;
import fr.esgi.doctodocapi.domain.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.domain.DomainException;
import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
import fr.esgi.doctodocapi.domain.entities.doctor.DoctorRepository;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.domain.entities.user.User;
import fr.esgi.doctodocapi.domain.entities.user.UserRepository;
import fr.esgi.doctodocapi.domain.use_cases.doctor.manage_medical_concern.manage_question.GetAllQuestions;
import fr.esgi.doctodocapi.domain.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all medical concerns associated with the currently authenticated doctor.
 * For each medical concern, it also fetches the list of related questions using the {@link GetAllQuestions} use case.
 */
@Service
public class GetAllMedicalConcerns {
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final GetAllQuestions getAllQuestions;

    public GetAllMedicalConcerns(MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, GetAllQuestions getAllQuestions) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.getAllQuestions = getAllQuestions;
    }

    /**
     * Executes the retrieval of all medical concerns for the authenticated doctor.
     * It resolves the doctor identity from the current user context, fetches associated medical concerns,
     * and for each concern, includes its related questions.
     *
     * @return a list of {@link GetMedicalConcernResponse} representing the medical concerns and their questions
     * @throws ApiException if the user or doctor cannot be found, or if a domain exception occurs
     */
    public List<GetMedicalConcernResponse> execute() {
        try {
            String username = this.getCurrentUserContext.getUsername();

            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<MedicalConcern> doctorConcerns = this.medicalConcernRepository.getMedicalConcerns(doctor);
            return doctorConcerns.stream()
                    .map(concern -> {
                        List<GetQuestionResponse> questions = this.getAllQuestions.execute(concern.getId());
                        return new GetMedicalConcernResponse(
                                concern.getId(),
                                concern.getName(),
                                concern.getDurationInMinutes().getValue(),
                                concern.getPrice(),
                                concern.getDoctorId(),
                                questions,
                                concern.getCreatedAt()
                        );
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
