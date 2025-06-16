package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetMedicalConcernResponse;

import java.util.List;

public interface IGetAllMedicalConcerns {
    List<GetMedicalConcernResponse> execute();
}
