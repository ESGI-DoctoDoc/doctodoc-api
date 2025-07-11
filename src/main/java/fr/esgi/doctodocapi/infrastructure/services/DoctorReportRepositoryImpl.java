package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorReportEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorReportJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorReportMapper;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import fr.esgi.doctodocapi.model.patient.DoctorReportRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DoctorReportRepositoryImpl implements DoctorReportRepository {
    private final DoctorReportMapper doctorReportMapper;
    private final EntityManager entityManager;
    private final DoctorReportJpaRepository doctorReportJpaRepository;

    public DoctorReportRepositoryImpl(DoctorReportMapper doctorReportMapper, EntityManager entityManager, DoctorReportJpaRepository doctorReportJpaRepository) {
        this.doctorReportMapper = doctorReportMapper;
        this.entityManager = entityManager;
        this.doctorReportJpaRepository = doctorReportJpaRepository;
    }

    @Override
    public void save(DoctorReport doctorReport) {
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, doctorReport.getDoctorId());
        UserEntity userEntity = this.entityManager.getReference(UserEntity.class, doctorReport.getReporterId());
        DoctorReportEntity entity = this.doctorReportMapper.toEntity(doctorReport, doctorEntity, userEntity);

        this.doctorReportJpaRepository.save(entity);
    }

    @Override
    public List<DoctorReport> getAllByDocteurId(UUID id) {
        List<DoctorReportEntity> entities = this.doctorReportJpaRepository.findAllByDoctor_Id(id);
        return entities.stream().map(doctorReportMapper::toDomain).toList();
    }
}
