package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientMapper {
    public PatientEntity toEntity(Patient patient, UserEntity user, DoctorEntity doctor) {
        PatientEntity entity = new PatientEntity();
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setEmail(patient.getEmail().getValue());
        entity.setPhoneNumber(patient.getPhoneNumber().getValue());
        entity.setBirthDate(patient.getBirthdate().getValue());
        entity.setMainAccount(patient.isMainAccount());
        entity.setUser(user);

        if (doctor != null) {
            entity.setDoctor(doctor);
        }

        return entity;
    }
}
