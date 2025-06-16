package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.SearchDoctorService;
import fr.esgi.doctodocapi.use_cases.patient.mappers.SearchDoctorPresentationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchDoctor {
    private final SearchDoctorService searchDoctorService;
    private final SearchDoctorPresentationMapper searchDoctorPresentationMapper;

    public SearchDoctor(SearchDoctorService searchDoctorService, SearchDoctorPresentationMapper searchDoctorPresentationMapper) {
        this.searchDoctorService = searchDoctorService;
        this.searchDoctorPresentationMapper = searchDoctorPresentationMapper;
    }

    public List<GetSearchDoctorResponse> process(String name, String speciality, List<String> languages, int page, int size) {
        List<Doctor> doctors = this.searchDoctorService.getDoctor(name, speciality, languages, page, size);
        return doctors.stream().map(this.searchDoctorPresentationMapper::toDto).toList();
    }
}
