package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorRecruitmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorRecruitmentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorRecruitmentMapper;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorRecruitmentRepositoryImpl implements DoctorRecruitmentRepository {
    private final DoctorRecruitmentJpaRepository doctorRecruitmentJpaRepository;
    private final DoctorRecruitmentMapper doctorRecruitmentMapper;

    public DoctorRecruitmentRepositoryImpl(DoctorRecruitmentJpaRepository doctorRecruitmentJpaRepository, DoctorRecruitmentMapper doctorRecruitmentMapper) {
        this.doctorRecruitmentJpaRepository = doctorRecruitmentJpaRepository;
        this.doctorRecruitmentMapper = doctorRecruitmentMapper;
    }

    @Override
    public void save(DoctorRecruitment doctorRecruitmentToSave) {
        DoctorRecruitmentEntity entityToSave = this.doctorRecruitmentMapper.toEntity(doctorRecruitmentToSave);
        this.doctorRecruitmentJpaRepository.save(entityToSave);
    }
}
