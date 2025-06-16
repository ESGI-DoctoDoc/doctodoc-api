package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.CreateMedicalConcernRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetMedicalConcernResponse;

public interface ICreateMedicalConcern {
    GetMedicalConcernResponse execute(CreateMedicalConcernRequest request);
}
