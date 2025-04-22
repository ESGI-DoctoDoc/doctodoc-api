package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DoctorMapper {
    public Doctor toDomain(DoctorEntity doctorEntity) {
        return new Doctor(
                doctorEntity.getUser().getId(),
                doctorEntity.getUser().getEmail(),
                doctorEntity.getUser().getPassword(),
                doctorEntity.getUser().getPhoneNumber(),
                doctorEntity.getUser().isEmailVerified(),
                doctorEntity.getUser().isDoubleAuthActive(),
                doctorEntity.getUser().isFirstConnexion(),
                doctorEntity.getUser().getDoubleAuthCode(),
                doctorEntity.getUser().getCreatedAt(),
                doctorEntity.getId(),
                doctorEntity.getRpps(),
                doctorEntity.getProfilePictureUrl(),
                doctorEntity.getBio(),
                Arrays.asList(doctorEntity.getSpecialities()),
                doctorEntity.getExperienceYears(),
                Arrays.asList(doctorEntity.getMedicalConcerns()),
                Arrays.asList(doctorEntity.getLanguages()),
                doctorEntity.getConsultationClinicPrice(),
                doctorEntity.getAddress(),
                doctorEntity.getClinicLatitude(),
                doctorEntity.getClinicLongitude()
        );
    }
}
