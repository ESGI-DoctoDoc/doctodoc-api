package fr.esgi.doctodocapi.use_cases.admin.search;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSpecialityResponse;
import org.springframework.stereotype.Service;

@Service
public class SearchAdminPresentationMapper {
    public GetSearchDoctorForAdminResponse toDoctorDto(Doctor doctor) {
        return new GetSearchDoctorForAdminResponse(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName() + " " + doctor.getPersonalInformations().getLastName()
        );
    }

    public GetSearchAppointmentResponse toAppointmentDto(Appointment appointment) {
        return new GetSearchAppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName()
        );
    }

    public GetSearchSpecialityResponse toSpecialityDto(Speciality speciality) {
        return new GetSearchSpecialityResponse(
                speciality.getId(),
                speciality.getName()
        );
    }
}