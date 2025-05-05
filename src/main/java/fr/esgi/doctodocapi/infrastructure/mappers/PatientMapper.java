package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class PatientMapper {
    private final DoctorMapper doctorMapper;

    public PatientMapper(DoctorMapper doctorMapper) {
        this.doctorMapper = doctorMapper;
    }

    public PatientEntity toEntity(Patient patient, UserEntity user, DoctorEntity doctor) {
        PatientEntity entity = new PatientEntity();
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setEmail(patient.getEmail().getValue());
        entity.setPhoneNumber(patient.getPhoneNumber().getValue());
        entity.setBirthDate(patient.getBirthdate().getValue());
        entity.setIsMainAccount(patient.isMainAccount());
        entity.setUser(user);

        if (doctor != null) {
            entity.setDoctor(doctor);
        }

        return entity;
    }

    public Patient toDomain(PatientEntity patientEntity) {
        Doctor doctor = null;

        if (patientEntity.getDoctor() != null) {
            doctor = this.doctorMapper.toDomain(patientEntity.getDoctor());
        }
        return new Patient(
                patientEntity.getUser().getId(),
                Email.of(patientEntity.getUser().getEmail()),
                null,
                PhoneNumber.of(patientEntity.getUser().getPhoneNumber()),
                patientEntity.getUser().isEmailVerified(),
                patientEntity.getUser().isDoubleAuthActive(),
                patientEntity.getUser().getDoubleAuthCode(),
                patientEntity.getUser().getCreatedAt(),
                patientEntity.getId(),
                doctor,
                patientEntity.getFirstName(),
                patientEntity.getLastName(),
                Email.of(patientEntity.getEmail()),
                PhoneNumber.of(patientEntity.getPhoneNumber()),
                Birthdate.of(patientEntity.getBirthDate()),
                patientEntity.getIsMainAccount()
        );
    }
}
