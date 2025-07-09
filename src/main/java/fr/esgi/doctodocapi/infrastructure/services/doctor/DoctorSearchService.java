package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.RetrieveDoctorSearchData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorSearchService implements RetrieveDoctorSearchData {
    private final CareTrackingRepository careTrackingRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorSearchService(CareTrackingRepository careTrackingRepository, MedicalConcernRepository medicalConcernRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<CareTracking> getCareTrackingByDoctor(UUID doctorId, String patientName, int page, int size) {
        String patientNameLower = (patientName == null || patientName.isBlank()) ? null : patientName.toLowerCase();
        return this.careTrackingRepository.findAllByDoctorAndPatientName(doctorId, patientNameLower, page, size);
    }

    public List<MedicalConcern> searchMedicalConcerns(UUID doctorId, String concernName, int page, int size) {
        String nameLower = (concernName == null || concernName.isBlank()) ? "" : concernName.toLowerCase();
        return this.medicalConcernRepository.searchByName(doctorId, nameLower, page, size);
    }


    public List<Patient> searchPatientsByDoctor(UUID doctorId, String name, int page, int size) {
        String nameLower = (name == null || name.isBlank()) ? null : name.toLowerCase();
        return this.patientRepository.searchByDoctorAndName(doctorId, nameLower, page, size);
    }

    public List<Appointment> searchAppointmentsByPatientName(UUID doctorId, String patientName, int page, int size) {
        String patientNameLower = (patientName == null || patientName.isBlank()) ? null : patientName.toLowerCase();
        return this.appointmentRepository.searchAppointmentsByDoctorAndPatientName(doctorId, patientNameLower, page, size);
    }
}