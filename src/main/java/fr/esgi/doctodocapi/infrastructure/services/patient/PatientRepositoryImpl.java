package fr.esgi.doctodocapi.infrastructure.services.patient;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PatientJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.PatientMapper;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientRepositoryImpl implements PatientRepository {
    private final PatientJpaRepository patientJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final PatientMapper patientMapper;

    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository, UserJpaRepository userJpaRepository,
                                 DoctorJpaRepository doctorJpaRepository, PatientMapper patientMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public Optional<Patient> getByUserId(UUID userId) {
        Optional<PatientEntity> patient = this.patientJpaRepository.findByUser_IdAndIsMainAccount(userId, true);

        if (patient.isPresent()) {
            PatientEntity entity = patient.get();
            return Optional.of(this.patientMapper.toDomain(entity));
        } else {
            return Optional.empty();
        }
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
        return this.patientJpaRepository.existsByUser_IdAndIsMainAccount(userId, true);
    }

    @Override
    public List<Patient> getCloseMembers(UUID id) {
        List<PatientEntity> closeMembers = this.patientJpaRepository.findAllByUser_IdAndIsMainAccount(id, false);
        return closeMembers.stream().map(this.patientMapper::toDomain).toList();
    }

    @Override
    public Patient getById(UUID id) {
        PatientEntity entity = this.patientJpaRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        return this.patientMapper.toDomain(entity);
    }

}
