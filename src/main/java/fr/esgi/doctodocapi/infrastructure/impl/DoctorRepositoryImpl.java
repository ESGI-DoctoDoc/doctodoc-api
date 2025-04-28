package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import org.springframework.stereotype.Service;

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
    public boolean isExistByUserId(UUID userId) {
        return this.doctorJpaRepository.existsByUser_Id(userId);
    }

    @Override
    public Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException {
        System.out.println(treatingDoctorId);
        DoctorEntity entity = this.doctorJpaRepository.findById(treatingDoctorId).orElseThrow(DoctorNotFoundException::new);
        return this.doctorMapper.toDomain(entity);
    }
}
