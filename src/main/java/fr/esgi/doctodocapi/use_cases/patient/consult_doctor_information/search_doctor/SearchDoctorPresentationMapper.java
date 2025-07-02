package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

@Service
public class SearchDoctorPresentationMapper {
    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public SearchDoctorPresentationMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public GetSearchDoctorResponse toDto(Doctor doctor) {
        String profileUrl = this.getDoctorProfileUrl.getUrl(doctor.getPersonalInformations().getProfilePictureUrl());
        return new GetSearchDoctorResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getProfessionalInformations().getSpeciality().getName(),
                profileUrl
        );
    }
}
