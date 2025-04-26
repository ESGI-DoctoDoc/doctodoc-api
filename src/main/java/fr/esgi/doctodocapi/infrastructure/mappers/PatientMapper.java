package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.user.email.Email;
import fr.esgi.doctodocapi.model.user.password.Password;
import fr.esgi.doctodocapi.model.user.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class PatientMapper {
    public Patient toDomain(PatientEntity patientEntity) {
        return new Patient(
                patientEntity.getUser().getId(),
                Email.of(patientEntity.getUser().getEmail()),
                Password.of(patientEntity.getUser().getPassword()),
                PhoneNumber.of(patientEntity.getUser().getPhoneNumber()),
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
