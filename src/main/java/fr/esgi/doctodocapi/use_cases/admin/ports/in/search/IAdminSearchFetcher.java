package fr.esgi.doctodocapi.use_cases.admin.ports.in.search;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSpecialityResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search.GetSearchSubscriptionResponse;

import java.util.List;

public interface IAdminSearchFetcher {
    List<GetSearchDoctorForAdminResponse> searchDoctors(String name, int page, int size);
    List<GetSearchAppointmentResponse> searchAppointments(String patientName, int page, int size);
    List<GetSearchSpecialityResponse> searchSpecialities(String name, int page, int size);
    List<GetSearchSubscriptionResponse> searchSubscriptions(String name, int page, int size);
}
