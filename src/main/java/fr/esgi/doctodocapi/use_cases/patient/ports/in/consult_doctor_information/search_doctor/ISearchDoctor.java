package fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.search_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;

import java.util.List;

public interface ISearchDoctor {
    List<GetSearchDoctorResponse> process(String name, String speciality, List<String> languages, int page, int size);
}
