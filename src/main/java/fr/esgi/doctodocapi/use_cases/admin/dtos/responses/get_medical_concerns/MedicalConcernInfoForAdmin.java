package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_medical_concerns;

import java.util.UUID;

public record MedicalConcernInfoForAdmin (
        UUID id,
        String name
){
}
