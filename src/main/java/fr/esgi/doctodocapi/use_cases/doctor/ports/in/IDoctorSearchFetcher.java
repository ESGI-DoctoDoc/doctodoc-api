package fr.esgi.doctodocapi.use_cases.doctor.ports.in;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetSearchAppointmentResponse;

import java.util.List;

public interface IDoctorSearchFetcher {
    List<GetDoctorCareTrackingResponse> searchCareTracking(String patientName, int page, int size);
    List<GetDoctorMedicalConcernResponse> searchMedicalConcerns(String concernName, int page, int size);
    List<GetDoctorPatientResponse> searchPatients(String name, int page, int size);
    List<GetSearchAppointmentResponse> searchAppointments(String patientName, int page, int size);
}
