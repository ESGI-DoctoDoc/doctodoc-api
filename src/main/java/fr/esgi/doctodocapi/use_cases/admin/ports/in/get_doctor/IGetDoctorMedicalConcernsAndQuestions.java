package fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response.GetAdminDoctorMedicalConcernsResponse;

import java.util.List;
import java.util.UUID;

public interface IGetDoctorMedicalConcernsAndQuestions {
    List<GetAdminDoctorMedicalConcernsResponse> get(UUID id);
}
