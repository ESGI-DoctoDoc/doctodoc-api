package fr.esgi.doctodocapi.domain.use_cases.patient.search_doctor;

import fr.esgi.doctodocapi.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
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
