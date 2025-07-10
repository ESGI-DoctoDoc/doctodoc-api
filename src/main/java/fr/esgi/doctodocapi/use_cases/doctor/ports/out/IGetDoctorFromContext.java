package fr.esgi.doctodocapi.use_cases.doctor.ports.out;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;

public interface IGetDoctorFromContext {
    Doctor get() throws DoctorNotFoundException;
}
