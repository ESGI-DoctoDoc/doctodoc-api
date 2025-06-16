package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionsInputRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;

import java.util.List;
import java.util.UUID;

public interface ISynchronizeQuestions {
    List<GetQuestionResponse> execute(UUID medicalConcernId, QuestionsInputRequest request);
}
