package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.Calendar;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.DoctorConsultationInformations;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.personal_information.CoordinatesGps;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.model.doctor.personal_information.Gender;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.DoctorProfessionalInformations;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorMapper {
    private final MedicalConcernMapper medicalConcernMapper;

    public DoctorMapper(MedicalConcernMapper medicalConcernMapper) {
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Doctor toDomain(DoctorEntity entity, List<Slot> slots, List<Absence> absences, List<MedicalConcern> medicalConcerns, List<Document> documents, Speciality speciality) {

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
                speciality,
                entity.getExperienceYears(),
                entity.getLanguages(),
                documents,
                entity.isAcceptPublicCoverage()
        );

        DoctorConsultationInformations consultationInformations = new DoctorConsultationInformations(
                entity.getConsultationClinicPrice(),
                entity.getAddress(),
                CoordinatesGps.of(entity.getClinicLatitude(), entity.getClinicLongitude()),
                medicalConcerns
        );

        Calendar calendar = new Calendar(slots, absences);

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
                entity.isVerified(),
                calendar,
                entity.getCustomerId(),
                entity.isRefused()
        );
    }


    public DoctorEntity toEntity(Doctor doctor, UserEntity userEntity) {
        DoctorPersonnalInformations personnalInformations = doctor.getPersonalInformations();
        DoctorProfessionalInformations professionalInformations = doctor.getProfessionalInformations();
        DoctorConsultationInformations consultationInformations = doctor.getConsultationInformations();
        CoordinatesGps coords = consultationInformations.getCoordinatesGps();

        DoctorEntity entity = new DoctorEntity();
        SpecialityEntity specialityEntity = new SpecialityEntity();

        entity.setUser(userEntity);
        entity.setId(doctor.getUserId());
        entity.setProfilePictureUrl(personnalInformations.getProfilePictureUrl());
        entity.setFirstName(personnalInformations.getFirstName());
        entity.setLastName(personnalInformations.getLastName());
        entity.setBirthDate(personnalInformations.getBirthDate().getValue());
        entity.setGender(personnalInformations.getGender().name());
        entity.setRpps(professionalInformations.getRpps().getValue());
        entity.setBio(professionalInformations.getBio());
        specialityEntity.setId(professionalInformations.getSpeciality().getId());
        entity.setSpeciality(specialityEntity);
        entity.setExperienceYears(professionalInformations.getExperienceYears().getValue());
        entity.setAcceptPublicCoverage(professionalInformations.isAcceptPublicCoverage());

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
        entity.setCustomerId(doctor.getCustomerId());
        entity.setRefused(doctor.isRefused());

        return entity;
    }
}