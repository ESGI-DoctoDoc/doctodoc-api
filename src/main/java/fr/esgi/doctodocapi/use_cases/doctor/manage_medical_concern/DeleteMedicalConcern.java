package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.DeleteMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.IDeleteMedicalConcern;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeleteMedicalConcern implements IDeleteMedicalConcern {

    private final MedicalConcernRepository repository;

    public DeleteMedicalConcern(MedicalConcernRepository repository) {
        this.repository = repository;
    }

    public DeleteMedicalConcernResponse execute(UUID concernId) {
        try {
            this.repository.delete(concernId);
            return new DeleteMedicalConcernResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}