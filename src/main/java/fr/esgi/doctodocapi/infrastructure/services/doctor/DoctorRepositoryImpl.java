package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorRepositoryImpl implements DoctorRepository {
    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorMapper doctorMapper;
    private final UserJpaRepository userJpaRepository;


    public DoctorRepositoryImpl(DoctorJpaRepository doctorJpaRepository, DoctorMapper doctorMapper, UserJpaRepository userJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.doctorMapper = doctorMapper;
        this.userJpaRepository = userJpaRepository;
    }


    @Override
    public Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException {
        DoctorEntity entity = this.doctorJpaRepository.findById(treatingDoctorId).orElseThrow(DoctorNotFoundException::new);
        return this.doctorMapper.toDomain(entity);
    }

    @Override
    public boolean isExistsById(UUID doctorId) {
        return this.doctorJpaRepository.existsById(doctorId);
    }

    @Override
    public Optional<Doctor> getByUserId(UUID userId) {
        Optional<DoctorEntity> doctor = this.doctorJpaRepository.findByUser_Id(userId);

        if (doctor.isPresent()) {
            DoctorEntity entity = doctor.get();
            return Optional.of(this.doctorMapper.toDomain(entity));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void save(Doctor doctor) {
        UserEntity user = this.userJpaRepository.findById(doctor.getUserId()).orElseThrow(UserNotFoundException::new);
        DoctorEntity entityToSaved = this.doctorMapper.toEntity(doctor, user);
        this.doctorJpaRepository.save(entityToSaved);
    }

    @Override
    public Doctor findDoctorByUserId(UUID doctorId) {
        DoctorEntity entity = this.doctorJpaRepository.findById(doctorId)
                .orElseThrow(DoctorNotFoundException::new);
        return doctorMapper.toDomain(entity);
    }

}
