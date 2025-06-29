package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTrackingFacadeMapper {
    private final CareTrackingMapper careTrackingMapper;
    private final PatientMapper patientMapper;
    private final CareTrackingTraceMapper careTrackingTraceMapper;

    public CareTrackingFacadeMapper(CareTrackingMapper careTrackingMapper, PatientMapper patientMapper, CareTrackingTraceMapper careTrackingTraceMapper) {
        this.careTrackingMapper = careTrackingMapper;
        this.patientMapper = patientMapper;
        this.careTrackingTraceMapper = careTrackingTraceMapper;
    }

    public CareTracking mapCareTrackingToDomain(CareTrackingEntity entity) {
        Patient patient = this.patientMapper.toDomain(entity.getPatient());

        List<CareTrackingTrace> traces = entity.getCareTrackingTraces()
                .stream()
                .map(careTrackingTraceMapper::toDomain)
                .toList();

        return this.careTrackingMapper.toDomain(entity, patient, traces);
    }
}
