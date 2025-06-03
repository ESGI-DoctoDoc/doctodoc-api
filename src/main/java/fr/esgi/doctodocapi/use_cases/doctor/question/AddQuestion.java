package fr.esgi.doctodocapi.use_cases.doctor.question;

import fr.esgi.doctodocapi.dtos.requests.doctor.question.AddQuestionRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.question.GetQuestionResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddQuestion {
    private final QuestionRepository questionRepository;
    private final MedicalConcernRepository medicalConcernRepository;

    public AddQuestion(QuestionRepository questionRepository, MedicalConcernRepository medicalConcernRepository) {
        this.questionRepository = questionRepository;
        this.medicalConcernRepository = medicalConcernRepository;
    }

    public GetQuestionResponse execute(UUID medicalConcernId, AddQuestionRequest addQuestionRequest) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);
            Question question = Question.create(
                    QuestionType.valueOf(addQuestionRequest.type()),
                    addQuestionRequest.question(),
                    addQuestionRequest.options(),
                    addQuestionRequest.isMandatory(),
                    medicalConcern.getId()
            );
            Question savedQuestion = this.questionRepository.save(question);
            return new GetQuestionResponse(
                    savedQuestion.getQuestion(),
                    savedQuestion.getType().getValue(),
                    savedQuestion.getOptions(),
                    savedQuestion.isMandatory(),
                    savedQuestion.getMedicalConcernId(),
                    savedQuestion.getCreatedAt()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
