package fr.esgi.doctodocapi.presentation.doctor.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionsInputRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IGetAllQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.ISynchronizeQuestions;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing medical concern questions related to doctors.
 */
@RequestMapping("doctors")
@RestController
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class ManageQuestionController {
    private final ISynchronizeQuestions synchronizeQuestions;
    private final IGetAllQuestions getAllQuestions;

    public ManageQuestionController(ISynchronizeQuestions synchronizeQuestions, IGetAllQuestions getAllQuestions) {
        this.synchronizeQuestions = synchronizeQuestions;
        this.getAllQuestions = getAllQuestions;
    }

    /**
     * Retrieves all questions.
     *
     * @return list of questions DTOs
     */
    @GetMapping("/medical-concerns/{id}/questions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetQuestionResponse> getAll(@PathVariable UUID id) {
        return this.getAllQuestions.execute(id);
    }

    /**
     * Adds a list of questions to a specific medical concern.
     *
     * @param id                 the UUID of the medical concern to which the questions are associated
     * @param addQuestionRequest list of questions to add
     * @return the list of created questions as DTO responses
     */
    @PostMapping("/medical-concerns/{id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public List<GetQuestionResponse> addQuestion(@PathVariable UUID id, @Valid @RequestBody QuestionsInputRequest addQuestionRequest) {
        return this.synchronizeQuestions.execute(id, addQuestionRequest);
    }
}
