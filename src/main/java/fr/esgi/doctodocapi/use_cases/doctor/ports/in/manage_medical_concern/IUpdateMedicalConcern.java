package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.UpdateMedicalConcernRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetUpdatedMedicalConcernResponse;

import java.util.UUID;

public interface IUpdateMedicalConcern {
    GetUpdatedMedicalConcernResponse execute(UUID id, UpdateMedicalConcernRequest request);
}
