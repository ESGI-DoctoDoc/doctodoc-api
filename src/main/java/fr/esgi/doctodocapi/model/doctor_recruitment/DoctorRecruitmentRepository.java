package fr.esgi.doctodocapi.model.doctor_recruitment;

import java.util.List;

public interface DoctorRecruitmentRepository {
    void save(DoctorRecruitment doctorRecruitment);

    List<DoctorRecruitment> getAll();

}
