package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorRecruitmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorRecruitmentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorRecruitmentMapper;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the DoctorRecruitmentRepository interface.
 * This service provides functionality to manage doctor recruitment data,
 * allowing the application to store information about doctors who are being recruited.
 */
@Repository
public class DoctorRecruitmentRepositoryImpl implements DoctorRecruitmentRepository {
    /**
     * Repository for accessing doctor recruitment data in the database.
     */
    private final DoctorRecruitmentJpaRepository doctorRecruitmentJpaRepository;

    /**
     * Mapper for converting between doctor recruitment domain objects and entities.
     */
    private final DoctorRecruitmentMapper doctorRecruitmentMapper;

    /**
     * Constructs a DoctorRecruitmentRepositoryImpl with the required repository and mapper.
     *
     * @param doctorRecruitmentJpaRepository Repository for doctor recruitment data access
     * @param doctorRecruitmentMapper        Mapper for doctor recruitment domain objects and entities
     */
    public DoctorRecruitmentRepositoryImpl(DoctorRecruitmentJpaRepository doctorRecruitmentJpaRepository, DoctorRecruitmentMapper doctorRecruitmentMapper) {
        this.doctorRecruitmentJpaRepository = doctorRecruitmentJpaRepository;
        this.doctorRecruitmentMapper = doctorRecruitmentMapper;
    }

    /**
     * Saves doctor recruitment information to the database.
     * This method converts the domain object to an entity before saving.
     *
     * @param doctorRecruitmentToSave The doctor recruitment information to save
     */
    @Override
    public void save(DoctorRecruitment doctorRecruitmentToSave) {
        DoctorRecruitmentEntity entityToSave = this.doctorRecruitmentMapper.toEntity(doctorRecruitmentToSave);
        this.doctorRecruitmentJpaRepository.save(entityToSave);
    }
}
