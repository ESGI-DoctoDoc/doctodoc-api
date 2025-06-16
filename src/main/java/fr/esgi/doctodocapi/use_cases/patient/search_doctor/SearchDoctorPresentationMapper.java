package fr.esgi.doctodocapi.use_cases.patient.search_doctor;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import org.springframework.stereotype.Service;

@Service
public class SearchDoctorPresentationMapper {
    public GetSearchDoctorResponse toDto(Doctor doctor) {
        return new GetSearchDoctorResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getProfessionalInformations().getSpeciality(),
                doctor.getPersonalInformations().getProfilePictureUrl()
        );
    }
}
