package fr.esgi.doctodocapi.infrastructure.jpa.mapper;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientMapper {
    public Patient toDomain(PatientEntity patientEntity) {
        return new Patient(
                patientEntity.getUser().getId(),
                patientEntity.getUser().getEmail(),
                patientEntity.getUser().getPassword(),
                patientEntity.getUser().getPhoneNumber(),
                patientEntity.getUser().isEmailVerified(),
                patientEntity.getUser().isDoubleAuthActive(),
                patientEntity.getUser().getDoubleAuthCode(),
                patientEntity.getUser().getCreatedAt(),
                patientEntity.getId(),
                patientEntity.getFirstName(),
                patientEntity.getLastName(),
                patientEntity.getEmail(),
                patientEntity.getPhoneNumber(),
                patientEntity.getBirthDate(),
                patientEntity.isMainAccount()

        );

    }
}
