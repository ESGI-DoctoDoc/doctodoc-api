package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.PatientMapper;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientRepositoryImpl implements PatientRepository {
    private final PatientJpaRepository patientJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final PatientMapper patientMapper;

    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository, UserJpaRepository userJpaRepository, DoctorJpaRepository doctorJpaRepository, PatientMapper patientMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public boolean isExistByUserId(UUID userId) {
        return this.patientJpaRepository.existsByUser_Id(userId);
    }

    @Override
    public void save(Patient patient) {
        UserEntity user = this.userJpaRepository.findById(patient.getUserId()).orElseThrow(UserNotFoundException::new);

        DoctorEntity doctor = null;
        if (patient.getDoctor() != null) {
            doctor =
                    this.doctorJpaRepository.findById(patient.getDoctor().getId()).orElseThrow(UserNotFoundException::new);
        }

        PatientEntity entityToSaved = this.patientMapper.toEntity(patient, user, doctor);

        this.patientJpaRepository.save(entityToSaved);
    }

    @Override
    public boolean isExistMainAccount(UUID userId) {
        return this.patientJpaRepository.existsByUser_idAndMainAccount(userId, true);
    }

}
