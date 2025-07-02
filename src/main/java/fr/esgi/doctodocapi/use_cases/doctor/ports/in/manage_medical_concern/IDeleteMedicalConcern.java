package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.DeleteMedicalConcernResponse;

import java.util.UUID;

public interface IDeleteMedicalConcern {
    DeleteMedicalConcernResponse execute(UUID concernId);
}
