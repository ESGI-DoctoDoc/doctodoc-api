package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorReportEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.doctor_report.DoctorReport;
import org.springframework.stereotype.Service;

@Service
public class DoctorReportMapper {
    public DoctorReportEntity toEntity(DoctorReport doctorReport, DoctorEntity doctorEntity, UserEntity userEntity) {
        DoctorReportEntity entity = new DoctorReportEntity();
        entity.setDoctorEntity(doctorEntity);
        entity.setUserEntity(userEntity);
        entity.setStatus(doctorReport.getStatus().name());
        entity.setExplanation(doctorReport.getExplanation());
        entity.setReportedAt(doctorReport.getReportedAt());

        return entity;
    }
}
