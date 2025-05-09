package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
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
                doctorEntity.getDoctorStatus(),
                doctorEntity.getProfilePictureUrl(),
                doctorEntity.getBio(),
                doctorEntity.getFirstName(),
                doctorEntity.getLastName(),
                Birthdate.of(doctorEntity.getBirthDate()),
                doctorEntity.getSpeciality(),
                doctorEntity.getExperienceYears(),
                Arrays.asList(doctorEntity.getMedicalConcerns()),
                Arrays.asList(doctorEntity.getLanguages()),
                doctorEntity.getConsultationClinicPrice(),
                doctorEntity.getAddress(),
                doctorEntity.getClinicLatitude(),
                doctorEntity.getClinicLongitude(),
                doctorEntity.isVerified(),
                doctorEntity.isAcceptPublicCoverage(),
                Arrays.asList(doctorEntity.getDoctorDocuments())
        );
    }


    public DoctorEntity toEntity(Doctor doctor, UserEntity userEntity) {
        DoctorEntity entity = new DoctorEntity();
        entity.setUser(userEntity);
        entity.setId(doctor.getId());
        entity.setRpps(doctor.getRpps());
        entity.setDoctorStatus(doctor.getDoctorStatus());
        entity.setProfilePictureUrl(doctor.getProfilePictureUrl());
        entity.setBio(doctor.getBio());
        entity.setFirstName(doctor.getFirstName());
        entity.setLastName(doctor.getLastName());
        entity.setBirthDate(doctor.getBirthDate().getValue());
        entity.setSpeciality(doctor.getSpecialty());
        entity.setExperienceYears(doctor.getExperienceYears());
        entity.setMedicalConcerns(doctor.getMedicalConcerns().toArray(new String[0]));
        entity.setLanguages(doctor.getLanguages().toArray(new String[0]));
        entity.setConsultationClinicPrice(doctor.getConsultationClinicPrice());
        entity.setAddress(doctor.getAddress());
        entity.setClinicLatitude(doctor.getClinicLatitude());
        entity.setClinicLongitude(doctor.getClinicLongitude());
        entity.setVerified(doctor.isVerified());
        entity.setAcceptPublicCoverage(doctor.isAcceptPublicCoverage());
        entity.setDoctorDocuments(doctor.getDoctorDocuments().toArray(new String[0]));

        return entity;
    }
}
