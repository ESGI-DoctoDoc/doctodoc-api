package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information.search_doctor;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.SearchDoctorService;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.search_doctor.ISearchDoctor;

import java.util.List;

public class SearchDoctor implements ISearchDoctor {
    private final SearchDoctorService searchDoctorService;
    private final SearchDoctorPresentationMapper searchDoctorPresentationMapper;

    public SearchDoctor(SearchDoctorService searchDoctorService, SearchDoctorPresentationMapper searchDoctorPresentationMapper) {
        this.searchDoctorService = searchDoctorService;
        this.searchDoctorPresentationMapper = searchDoctorPresentationMapper;
    }

    public List<GetSearchDoctorResponse> process(String name, String speciality, List<String> languages, boolean valid, int page, int size) {
        List<Doctor> doctors = this.searchDoctorService.getDoctor(name, speciality, languages, valid, page, size);
        return doctors.stream().map(this.searchDoctorPresentationMapper::toDto).toList();
    }
}
