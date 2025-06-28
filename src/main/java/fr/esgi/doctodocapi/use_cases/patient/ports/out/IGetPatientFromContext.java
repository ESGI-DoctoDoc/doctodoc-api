package fr.esgi.doctodocapi.use_cases.patient.ports.out;

import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;

public interface IGetPatientFromContext {
    Patient get() throws PatientNotFoundException;
}
