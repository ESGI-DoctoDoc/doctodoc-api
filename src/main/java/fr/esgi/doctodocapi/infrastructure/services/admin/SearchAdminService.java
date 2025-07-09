package fr.esgi.doctodocapi.infrastructure.services.admin;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.invoice.DoctorInvoiceRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.RetrieveSearchData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchAdminService implements RetrieveSearchData {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final SpecialityRepository specialityRepository;

    public SearchAdminService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, SpecialityRepository specialityRepository, DoctorInvoiceRepository doctorInvoiceRepository, DoctorSubscriptionRepository doctorSubscriptionRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.specialityRepository = specialityRepository;
    }

    public List<Doctor> getDoctors(String name, int page, int size) {
        String nameLower = (name == null || name.isBlank()) ? null : name.toLowerCase();
        return this.doctorRepository.searchDoctorsByName(nameLower, page, size);
    }

    public List<Appointment> getAppointments(String patientName, int page, int size) {
        String patientNameLower = (patientName == null || patientName.isBlank()) ? null : patientName.toLowerCase();
        return this.appointmentRepository.searchAppointmentsByPatientName(patientNameLower, page, size);
    }

    public List<Speciality> getSpecialities(String name, int page, int size) {
        String nameLower = (name == null || name.isBlank()) ? null : name.toLowerCase();
        return this.specialityRepository.searchSpecialitiesByName(nameLower, page, size);
    }
}
