package fr.esgi.doctodocapi.use_cases.admin.ports.out;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.Doctor;

import java.util.List;

public interface RetrieveSearchData {
    List<Doctor> getDoctors(String name, int page, int size);
    List<Appointment> getAppointments(String patientName, int page, int size);
    List<Speciality> getSpecialities(String name, int page, int size);
}
