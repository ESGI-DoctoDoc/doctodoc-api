package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorRecruitmentEntity;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import org.springframework.stereotype.Service;

@Service
public class DoctorRecruitmentMapper {
    public DoctorRecruitmentEntity toEntity(DoctorRecruitment doctorRecruitment) {
        DoctorRecruitmentEntity entity = new DoctorRecruitmentEntity();
        entity.setFirstName(doctorRecruitment.getFirstName());
        entity.setLastName(doctorRecruitment.getLastName());

        return entity;
    }
}
