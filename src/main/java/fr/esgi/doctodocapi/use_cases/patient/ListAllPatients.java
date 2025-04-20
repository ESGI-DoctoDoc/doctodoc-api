package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllPatients {
    PatientRepository patientRepository;

    public ListAllPatients(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findAll() {
        // todo to dto
        return patientRepository.findAll();
    }

}
