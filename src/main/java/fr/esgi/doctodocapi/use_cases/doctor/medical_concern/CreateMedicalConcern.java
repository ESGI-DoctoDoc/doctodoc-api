package fr.esgi.doctodocapi.use_cases.doctor.medical_concern;

import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.CreateMedicalConcernRequest;
import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionsInputRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern.GetMedicalConcernResponse;
import fr.esgi.doctodocapi.dtos.responses.doctor.question.GetQuestionResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.medical_concern.question.AddQuestion;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for creating a new MedicalConcern (pour un doctor donné).
 * Si la requête contient des questions, on délègue leur création à AddQuestion.
 */
@Service
public class CreateMedicalConcern {
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final AddQuestion addQuestion;
    private final GetCurrentUserContext getCurrentUserContext;

    public CreateMedicalConcern(MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, AddQuestion addQuestion, GetCurrentUserContext getCurrentUserContext) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.addQuestion = addQuestion;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public GetMedicalConcernResponse execute(CreateMedicalConcernRequest createMedicalConcernRequest) {
        try {
            String username = this.getCurrentUserContext.getUsername();

            User doctor = this.userRepository.findByEmail(username);

            MedicalConcern newMedicalConcern = MedicalConcern.create(
                    createMedicalConcernRequest.name(),
                    createMedicalConcernRequest.duration(),
                    List.of(),
                    createMedicalConcernRequest.price(),
                    doctor.getId()
            );

            MedicalConcern savedConcern = this.medicalConcernRepository.save(newMedicalConcern);

            List<GetQuestionResponse> createdQuestions = List.of();
            if(createMedicalConcernRequest.questions() != null && !createMedicalConcernRequest.questions().isEmpty()) {
                QuestionsInputRequest questionsInputRequest = new QuestionsInputRequest(createMedicalConcernRequest.questions());
                createdQuestions = this.addQuestion.execute(savedConcern.getId(), questionsInputRequest);
            }

            return new GetMedicalConcernResponse(
                    savedConcern.getId(),
                    savedConcern.getName(),
                    savedConcern.getDurationInMinutes().getValue(),
                    savedConcern.getPrice(),
                    doctor.getId(),
                    createdQuestions,
                    savedConcern.getCreatedAt()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
