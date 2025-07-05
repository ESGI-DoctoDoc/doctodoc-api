package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.TokenFcmRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TokenFcmRepositoryImpl implements TokenFcmRepository {
    private final PatientJpaRepository patientJpaRepository;

    public TokenFcmRepositoryImpl(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public void save(UUID patientId, String tokenFcm) {
        PatientEntity entity = this.patientJpaRepository.findById(patientId).orElseThrow(PatientNotFoundException::new);
        entity.setTokenFcm(tokenFcm);
        this.patientJpaRepository.save(entity);
    }

    @Override
    public String get(UUID patientId) {
        PatientEntity entity = this.patientJpaRepository.findById(patientId).orElseThrow(PatientNotFoundException::new);
        return entity.getFcmToken();
    }
}