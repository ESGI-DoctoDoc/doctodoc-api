package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.mapper.PatientMapper;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientRepositoryImpl implements PatientRepository {
    private final PatientJpaRepository jpaRepository;
    private final PatientMapper patientMapper;

    public PatientRepositoryImpl(PatientJpaRepository jpaRepository, PatientMapper patientMapper) {
        this.jpaRepository = jpaRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public List<Patient> findAll() {
        List<PatientEntity> patientEntities = jpaRepository.findAll();
        return patientEntities.stream().map(this.patientMapper::toDomain).toList();
    }
}
