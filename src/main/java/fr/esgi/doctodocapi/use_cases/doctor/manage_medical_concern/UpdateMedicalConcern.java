package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.UpdateMedicalConcernRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.GetUpdatedMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.IUpdateMedicalConcern;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdateMedicalConcern implements IUpdateMedicalConcern {

    private final MedicalConcernRepository medicalConcernRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public UpdateMedicalConcern(MedicalConcernRepository medicalConcernRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    @Override
    public GetUpdatedMedicalConcernResponse execute(UUID medicalConcernId, UpdateMedicalConcernRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            MedicalConcern saved = this.medicalConcernRepository.update(
                    medicalConcernId,
                    doctor.getId(),
                    request.name(),
                    request.durationInMinutes(),
                    request.price()
            );

            return new GetUpdatedMedicalConcernResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getDurationInMinutes().getValue(),
                    saved.getPrice(),
                    saved.getCreatedAt()
            );


        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}