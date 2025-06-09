package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchDoctorService {
    private final DoctorRepository doctorRepository;

    public SearchDoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    public List<Doctor> getDoctor(String name, String speciality, List<String> languages, int page, int size) {
        String nameLower = (name == null) ? "" : name.toLowerCase();
        String specialityLower = (speciality == null || speciality.isBlank()) ? null : speciality.toLowerCase();
        return this.doctorRepository.searchDoctors(
                nameLower,
                specialityLower,
                languages,
                page, size
        );
    }

}
