package fr.esgi.doctodocapi.use_cases.admin.get_doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.DoctorResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorsForAdmin;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetDoctorsForAdmin implements IGetDoctorsForAdmin {

    private final DoctorRepository doctorRepository;
    private final DoctorResponseMapper doctorResponseMapper;
    private final SpecialityRepository specialityRepository;

    public GetDoctorsForAdmin(DoctorRepository doctorRepository, DoctorResponseMapper doctorResponseMapper, SpecialityRepository specialityRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorResponseMapper = doctorResponseMapper;
        this.specialityRepository = specialityRepository;
    }

    public List<GetDoctorForAdminResponse> execute(int page, int size) {
        try {
            List<Doctor> doctors = this.doctorRepository.findAllForAdmin(page, size);
            return doctors.stream()
                    .map(doctor -> {
                        String specialityName = doctor.getProfessionalInformations().getSpeciality();
                        Speciality speciality = this.specialityRepository.findByName(specialityName);
                        return this.doctorResponseMapper.toAdminResponse(doctor, speciality);
                    })
                    .toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
