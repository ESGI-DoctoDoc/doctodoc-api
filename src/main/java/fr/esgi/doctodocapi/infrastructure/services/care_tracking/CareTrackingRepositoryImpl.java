package fr.esgi.doctodocapi.infrastructure.services.care_tracking;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.CareTrackingJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMapper;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingNotFoundException;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CareTrackingRepositoryImpl implements CareTrackingRepository {
    private final CareTrackingJpaRepository careTrackingJpaRepository;
    private final CareTrackingMapper careTrackingMapper;
    private final EntityManager entityManager;
    private final CareTrackingFacadeMapper careTrackingFacadeMapper;

    public CareTrackingRepositoryImpl(CareTrackingJpaRepository careTrackingJpaRepository, CareTrackingMapper careTrackingMapper, EntityManager entityManager, CareTrackingFacadeMapper careTrackingFacadeMapper) {
        this.careTrackingJpaRepository = careTrackingJpaRepository;
        this.careTrackingMapper = careTrackingMapper;
        this.entityManager = entityManager;
        this.careTrackingFacadeMapper = careTrackingFacadeMapper;
    }

    @Override
    public UUID save(CareTracking careTracking) {
        DoctorEntity doctor = this.entityManager.getReference(DoctorEntity.class, careTracking.getCreatorId());
        PatientEntity patient = this.entityManager.getReference(PatientEntity.class, careTracking.getPatient().getId());

        CareTrackingEntity entity = this.careTrackingMapper.toEntity(careTracking, patient);

        List<AppointmentEntity> appointmentEntities = careTracking.getAppointmentsId()
                .stream()
                .map(id -> this.entityManager.getReference(AppointmentEntity.class, id))
                .toList();
        entity.setAppointments(appointmentEntities);
        entity.setCreator(doctor);

        CareTrackingEntity savedEntity = this.careTrackingJpaRepository.save(entity);
        return savedEntity.getId();
    }

    @Override
    public List<CareTracking> findAll(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.careTrackingJpaRepository.findAllByCreator_Id(doctorId, pageable)
                .stream()
                .map(careTrackingFacadeMapper::mapCareTrackingToDomain)
                .toList();
    }

    @Override
    public CareTracking getById(UUID id) throws CareTrackingNotFoundException {
        CareTrackingEntity careTrackingEntity = this.careTrackingJpaRepository.findById(id).orElseThrow(CareTrackingNotFoundException::new);
        return careTrackingFacadeMapper.mapCareTrackingToDomain(careTrackingEntity);
    }
}
