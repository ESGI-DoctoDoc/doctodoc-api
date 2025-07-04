package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.book_appointment_in_care_tracking.BookAppointmentInCareTracking;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.book_appointment_in_care_tracking.IBookAppointmentInCareTracking;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentInCareTrackingConfiguration {

    @Bean
    public IBookAppointmentInCareTracking bookAppointmentInCareTracking(CareTrackingRepository careTrackingRepository, AppointmentRepository appointmentRepository, SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, PatientRepository patientRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository) {
        return new BookAppointmentInCareTracking(careTrackingRepository, appointmentRepository, slotRepository, medicalConcernRepository, patientRepository, getCurrentUserContext, userRepository, doctorRepository);
    }
}
