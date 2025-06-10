package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.domain.entities.doctor.personal_information.CoordinatesGps;
import fr.esgi.doctodocapi.domain.entities.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.domain.entities.doctor.personal_information.Gender;
import fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.domain.entities.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.domain.entities.vo.email.Email;
import fr.esgi.doctodocapi.domain.entities.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorMapper {
    private final MedicalConcernMapper medicalConcernMapper;

    public DoctorMapper(MedicalConcernMapper medicalConcernMapper) {
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Doctor toDomain(DoctorEntity entity) {

        DoctorPersonnalInformations personnalInformations = new DoctorPersonnalInformations(
                entity.getProfilePictureUrl(),
                entity.getFirstName(),
                entity.getLastName(),
                Birthdate.of(entity.getBirthDate()),
                Gender.valueOf(entity.getGender())
        );

        DoctorProfessionalInformations professionalInformations = new DoctorProfessionalInformations(
                entity.getRpps(),
                entity.getBio(),
                entity.getSpeciality(),
                entity.getExperienceYears(),
                entity.getLanguages(),
                entity.getDoctorDocuments(),
                entity.isAcceptPublicCoverage()
        );

        List<MedicalConcern> medicalConcerns =
                entity.getMedicalConcerns().stream().map(medicalConcernMapper::toDomain).toList();

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                entity.getConsultationClinicPrice(),
                entity.getAddress(),
                CoordinatesGps.of(entity.getClinicLatitude(), entity.getClinicLongitude()),
                medicalConcerns
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
                personnalInformations,
                professionalInformations,
                consultationInformations,
                entity.isVerified()
        );
    }


    public DoctorEntity toEntity(Doctor doctor, UserEntity userEntity) {
        DoctorPersonnalInformations personnalInformations = doctor.getPersonalInformations();
        DoctorProfessionalInformations professionalInformations = doctor.getProfessionalInformations();
        DoctorConsultationInformations consultationInformations = doctor.getConsultationInformations();
        CoordinatesGps coords = consultationInformations.getCoordinatesGps();


        DoctorEntity entity = new DoctorEntity();
        entity.setUser(userEntity);
        entity.setId(doctor.getUserId());
        entity.setRpps(professionalInformations.getRpps().getValue());
        entity.setProfilePictureUrl(personnalInformations.getProfilePictureUrl());
        entity.setBio(professionalInformations.getBio());
        entity.setFirstName(personnalInformations.getFirstName());
        entity.setLastName(personnalInformations.getLastName());
        entity.setBirthDate(personnalInformations.getBirthDate().getValue());
        entity.setGender(personnalInformations.getGender().name());
        entity.setSpeciality(professionalInformations.getSpeciality());
        entity.setExperienceYears(professionalInformations.getExperienceYears().getValue());

        List<MedicalConcernEntity> medicalConcerns =
                consultationInformations.getMedicalConcerns().stream().map(medicalConcern -> this.medicalConcernMapper.toEntity(
                        medicalConcern, entity
                )).toList();
        entity.setMedicalConcerns(medicalConcerns);

        entity.setLanguages(professionalInformations.getLanguages());
        entity.setConsultationClinicPrice(consultationInformations.getPrice());
        entity.setAddress(consultationInformations.getAddress());
        if(coords != null) {
            entity.setClinicLatitude(consultationInformations.getCoordinatesGps().getClinicLatitude());
            entity.setClinicLongitude(consultationInformations.getCoordinatesGps().getClinicLongitude());
        }
        entity.setVerified(doctor.isVerified());
        entity.setAcceptPublicCoverage(professionalInformations.isAcceptPublicCoverage());
        entity.setDoctorDocuments(professionalInformations.getDoctorDocuments());

        return entity;
    }
}