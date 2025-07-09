package fr.esgi.doctodocapi.use_cases.doctor.ports.out;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;

import java.util.List;
import java.util.UUID;

public interface RetrieveDoctorSearchData {
    List<CareTracking> getCareTrackingByDoctor(UUID doctorId, String patientName, int page, int size);
    List<MedicalConcern> searchMedicalConcerns(UUID doctorId, String concernName, int page, int size);
    List<Patient> searchPatientsByDoctor(UUID doctorId, String name, int page, int size);
    List<Appointment> searchAppointmentsByPatientName(UUID doctorId, String patientName, int page, int size);
}
