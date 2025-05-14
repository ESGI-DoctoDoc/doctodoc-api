package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorStatus;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.model.doctor.personal_information.CoordinatesGps;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class DoctorMapper {
    public Doctor toDomain(DoctorEntity entity) {

        DoctorPersonnalInformations personnalInformations = new DoctorPersonnalInformations(
                entity.getProfilePictureUrl(),
                entity.getFirstName(),
                entity.getLastName(),
                Birthdate.of(entity.getBirthDate())
        );

        DoctorProfessionalInformations professionalInformations = new DoctorProfessionalInformations(
                entity.getRpps(),
                entity.getBio(),
                entity.getSpeciality(),
                entity.getExperienceYears(),
                entity.getLanguages(),
                entity.getDoctorDocuments()
        );

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                entity.getConsultationClinicPrice(),
                entity.getAddress(),
                CoordinatesGps.of(entity.getClinicLatitude(), entity.getClinicLongitude()),
                entity.getMedicalConcerns()
        );

        return new Doctor(
                entity.getUser().getId(),
                Email.of(entity.getUser().getEmail()),
                null,
                PhoneNumber.of(entity.getUser().getPhoneNumber()),
                entity.getUser().isEmailVerified(),
                entity.getUser().isDoubleAuthActive(),
                entity.getUser().getDoubleAuthCode(),
                entity.getUser().getCreatedAt(),
                entity.getId(),
                DoctorStatus.fromValue(entity.getDoctorStatus()),
                personnalInformations,
                professionalInformations,
                consultationInformations,
                entity.isVerified(),
                entity.isAcceptPublicCoverage()

        );
    }


    public DoctorEntity toEntity(Doctor doctor, UserEntity userEntity) {
        DoctorPersonnalInformations personnalInformations = doctor.getPersonalInformations();
        DoctorProfessionalInformations professionalInformations = doctor.getProfessionalInformations();
        DoctorConsultationInformations consultationInformations = doctor.getConsultationInformations();

        DoctorEntity entity = new DoctorEntity();
        entity.setUser(userEntity);
        entity.setId(doctor.getId());
        entity.setRpps(professionalInformations.getRpps().getValue());
        entity.setDoctorStatus(doctor.getDoctorStatus().getValue());
        entity.setProfilePictureUrl(personnalInformations.getProfilePictureUrl());
        entity.setBio(professionalInformations.getBio());
        entity.setFirstName(personnalInformations.getFirstName());
        entity.setLastName(personnalInformations.getLastName());
        entity.setBirthDate(personnalInformations.getBirthDate().getValue());
        entity.setSpeciality(professionalInformations.getSpeciality());
        entity.setExperienceYears(professionalInformations.getExperienceYears());
        entity.setMedicalConcerns(consultationInformations.getMedicalConcerns());
        entity.setLanguages(professionalInformations.getLanguages());
        entity.setConsultationClinicPrice(consultationInformations.getPrice());
        entity.setAddress(consultationInformations.getAddress());
        entity.setClinicLatitude(consultationInformations.getCoordinatesGps().getClinicLatitude());
        entity.setClinicLongitude(consultationInformations.getCoordinatesGps().getClinicLongitude());
        entity.setVerified(doctor.isVerified());
        entity.setAcceptPublicCoverage(doctor.isAcceptPublicCoverage());
        entity.setDoctorDocuments(professionalInformations.getDoctorDocuments());

        return entity;
    }
}