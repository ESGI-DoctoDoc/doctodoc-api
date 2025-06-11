package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.patient.mappers.GetDetailDoctorPresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetDoctorDetail {
    private final DoctorRepository doctorRepository;
    private final GetDetailDoctorPresentationMapper getDetailDoctorPresentationMapper;

    public GetDoctorDetail(DoctorRepository doctorRepository, GetDetailDoctorPresentationMapper getDetailDoctorPresentationMapper) {
        this.doctorRepository = doctorRepository;
        this.getDetailDoctorPresentationMapper = getDetailDoctorPresentationMapper;
    }

    public GetDoctorDetailResponse get(UUID id) {
        try {
            Doctor doctor = this.doctorRepository.getById(id);
            return this.getDetailDoctorPresentationMapper.toDto(doctor);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
