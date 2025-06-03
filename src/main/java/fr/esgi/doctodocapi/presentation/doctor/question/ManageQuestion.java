package fr.esgi.doctodocapi.presentation.doctor.question;

import fr.esgi.doctodocapi.dtos.requests.doctor.question.AddQuestionRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.question.GetQuestionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.question.AddQuestion;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("doctors")
@RestController
public class ManageQuestion {
    private final AddQuestion addQuestion;

    public ManageQuestion(AddQuestion addQuestion) {
        this.addQuestion = addQuestion;
    }

    @PostMapping("/{medicalConcernId}/questions")
    public GetQuestionResponse addQuestion(@PathVariable UUID medicalConcernId, @Valid @RequestBody AddQuestionRequest addQuestionRequest) {
        return this.addQuestion.execute(medicalConcernId, addQuestionRequest);
    }
}
