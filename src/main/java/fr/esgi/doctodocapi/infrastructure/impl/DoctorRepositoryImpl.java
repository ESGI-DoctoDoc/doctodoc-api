package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorRepositoryImpl implements DoctorRepository {
    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorMapper doctorMapper;

    public DoctorRepositoryImpl(DoctorJpaRepository doctorJpaRepository, DoctorMapper doctorMapper) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException {
        DoctorEntity entity = this.doctorJpaRepository.findById(treatingDoctorId).orElseThrow(DoctorNotFoundException::new);
        return this.doctorMapper.toDomain(entity);
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
}
