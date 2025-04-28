package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DoctorMapper {
    public Doctor toDomain(DoctorEntity doctorEntity) {
        return new Doctor(
                doctorEntity.getUser().getId(),
                Email.of(doctorEntity.getUser().getEmail()),
                Password.of(doctorEntity.getUser().getPassword()),
                PhoneNumber.of(doctorEntity.getUser().getPhoneNumber()),
                doctorEntity.getUser().isEmailVerified(),
                doctorEntity.getUser().isDoubleAuthActive(),
                doctorEntity.getUser().getDoubleAuthCode(),
                doctorEntity.getUser().getCreatedAt(),
                doctorEntity.getId(),
                doctorEntity.getRpps(),
                doctorEntity.getProfilePictureUrl(),
                doctorEntity.getBio(),
                doctorEntity.getFirstName(),
                doctorEntity.getLastName(),
                doctorEntity.getSpecialities(),
                doctorEntity.getExperienceYears(),
                Arrays.asList(doctorEntity.getMedicalConcerns()),
                Arrays.asList(doctorEntity.getLanguages()),
                doctorEntity.getConsultationClinicPrice(),
                doctorEntity.getAddress(),
                doctorEntity.getClinicLatitude(),
                doctorEntity.getClinicLongitude(),
                doctorEntity.isVerified()
        );
    }
}
