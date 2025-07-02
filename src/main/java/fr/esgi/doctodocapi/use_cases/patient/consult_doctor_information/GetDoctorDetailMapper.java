package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.GetAddressDoctorResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.GetDoctorDetailResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.OpeningHoursResponse;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDoctorDetailMapper {
    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public GetDoctorDetailMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }
    public GetDoctorDetailResponse toDto(Doctor doctor, List<OpeningHoursResponse> openingHours) {
        GetAddressDoctorResponse address = new GetAddressDoctorResponse(
                doctor.getConsultationInformations().getAddress(),
                doctor.getConsultationInformations().getCoordinatesGps().getClinicLatitude(),
                doctor.getConsultationInformations().getCoordinatesGps().getClinicLongitude()
        );

        String profileUrl = this.getDoctorProfileUrl.getUrl(doctor.getPersonalInformations().getProfilePictureUrl());

        return new GetDoctorDetailResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getProfessionalInformations().getSpeciality().getName(),
                doctor.getProfessionalInformations().getBio(),
                profileUrl,
                doctor.getProfessionalInformations().getRpps().getValue(),
                address,
                doctor.getProfessionalInformations().getLanguages(),
                openingHours
        );
    }

}
