package fr.esgi.doctodocapi.use_cases.admin.get_doctor;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response.GetAdminDoctorMedicalConcernsResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response.GetAdminDoctorQuestionsResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorMedicalConcernsAndQuestions;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GetDoctorMedicalConcernsAndQuestions implements IGetDoctorMedicalConcernsAndQuestions {
    private final DoctorRepository doctorRepository;
    private final MedicalConcernRepository medicalConcernRepository;

    public GetDoctorMedicalConcernsAndQuestions(DoctorRepository doctorRepository, MedicalConcernRepository medicalConcernRepository) {
        this.doctorRepository = doctorRepository;
        this.medicalConcernRepository = medicalConcernRepository;
    }

    public List<GetAdminDoctorMedicalConcernsResponse> get(UUID id) {
        try {
            Doctor doctor = this.doctorRepository.getById(id);
            List<MedicalConcern> medicalConcerns = this.medicalConcernRepository.getMedicalConcerns(doctor);
            return medicalConcerns.stream().map(medicalConcern -> {
                List<Question> questions = medicalConcern.getQuestions();

                List<GetAdminDoctorQuestionsResponse> questionsResponses = questions
                        .stream()
                        .map(question -> {
                            List<Map<String, String>> mappedOptions = question.getOptions() == null ? List.of() :
                                    question.getOptions().stream()
                                            .map(option -> Map.of(
                                                    "label", option,
                                                    "value", option.toLowerCase()
                                            ))
                                            .toList();


                            return new GetAdminDoctorQuestionsResponse(
                                    question.getId(),
                                    question.getQuestion(),
                                    question.getType().getValue(),
                                    mappedOptions,
                                    question.isMandatory(),
                                    question.getCreatedAt()
                            );
                        })
                        .toList();

                return new GetAdminDoctorMedicalConcernsResponse(
                        medicalConcern.getId(),
                        medicalConcern.getName(),
                        medicalConcern.getDurationInMinutes().getValue(),
                        medicalConcern.getPrice(),
                        questionsResponses,
                        medicalConcern.getCreatedAt()
                );
            }).toList();


        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
