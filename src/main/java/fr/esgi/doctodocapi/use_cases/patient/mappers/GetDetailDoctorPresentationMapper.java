package fr.esgi.doctodocapi.use_cases.patient.mappers;

import fr.esgi.doctodocapi.dtos.responses.doctor_detail_reponse.GetAddressDoctorResponse;
import fr.esgi.doctodocapi.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDetailDoctorPresentationMapper {
    public GetDoctorDetailResponse toDto(Doctor doctor) {
        GetAddressDoctorResponse address = new GetAddressDoctorResponse(
                doctor.getConsultationInformations().getAddress(),
                doctor.getConsultationInformations().getCoordinatesGps().getClinicLatitude(),
                doctor.getConsultationInformations().getCoordinatesGps().getClinicLongitude()
        );

        return new GetDoctorDetailResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getProfessionalInformations().getSpeciality(),
                doctor.getProfessionalInformations().getBio(),
                doctor.getPersonalInformations().getProfilePictureUrl(),
                doctor.getProfessionalInformations().getRpps().getValue(),
                address,
                doctor.getProfessionalInformations().getLanguages(),
                List.of()
        );
    }

}
