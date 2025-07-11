package fr.esgi.doctodocapi.use_cases.admin.get_doctor_recruitment;

import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitment;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetDoctorsToRecruitResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor_recruitment.IGetDoctorsToRecruit;

import java.util.List;

public class GetDoctorsToRecruit implements IGetDoctorsToRecruit {
    private final DoctorRecruitmentRepository doctorRecruitmentRepository;

    public GetDoctorsToRecruit(DoctorRecruitmentRepository doctorRecruitmentRepository) {
        this.doctorRecruitmentRepository = doctorRecruitmentRepository;
    }

    public List<GetDoctorsToRecruitResponse> get() {
        List<DoctorRecruitment> doctorRecruitments = this.doctorRecruitmentRepository.getAll();
        return doctorRecruitments.stream().map(doctorRecruitment ->
                new GetDoctorsToRecruitResponse(
                        doctorRecruitment.getId(),
                        doctorRecruitment.getFirstName(),
                        doctorRecruitment.getLastName(),
                        doctorRecruitment.getCreatedAt()
                )
        ).toList();

    }
}
