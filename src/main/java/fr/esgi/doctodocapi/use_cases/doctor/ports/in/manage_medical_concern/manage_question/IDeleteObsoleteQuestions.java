package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question;

import java.util.List;
import java.util.UUID;

public interface IDeleteObsoleteQuestions {
    void execute(UUID medicalConcernId, List<UUID> incomingIds);
}
