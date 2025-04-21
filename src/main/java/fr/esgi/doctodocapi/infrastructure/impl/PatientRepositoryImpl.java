package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientRepositoryImpl implements PatientRepository {
    private final PatientJpaRepository patientJpaRepository;

    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public boolean isExistPatientByUserId(UUID userId) {
        return this.patientJpaRepository.existsByUser_Id(userId);
    }

}
