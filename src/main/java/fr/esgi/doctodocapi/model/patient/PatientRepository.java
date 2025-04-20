package fr.esgi.doctodocapi.model.patient;

import java.util.List;

public interface PatientRepository {
    List<Patient> findAll();
}
