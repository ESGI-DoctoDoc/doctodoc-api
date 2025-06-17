package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.OpeningHoursResponse;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.IGetDoctorDetail;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class GetDoctorDetail implements IGetDoctorDetail {
    private final DoctorRepository doctorRepository;
    private final GetDoctorDetailMapper getDoctorDetailMapper;
    private final OpeningHoursCalculator openingHoursCalculator;

    public GetDoctorDetail(DoctorRepository doctorRepository, GetDoctorDetailMapper getDoctorDetailMapper, OpeningHoursCalculator openingHoursCalculator) {
        this.doctorRepository = doctorRepository;
        this.getDoctorDetailMapper = getDoctorDetailMapper;
        this.openingHoursCalculator = openingHoursCalculator;
    }

    public GetDoctorDetailResponse get(UUID id) {
        try {
            Doctor doctor = this.doctorRepository.getById(id);
            List<OpeningHoursResponse> openingHours = this.openingHoursCalculator.getOpeningHoursOfCurrentWeek(doctor.getCalendar().getSlots());
            return this.getDoctorDetailMapper.toDto(doctor, openingHours);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
