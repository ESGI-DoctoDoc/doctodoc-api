package fr.esgi.doctodocapi.domain.entities.appointment;

import fr.esgi.doctodocapi.domain.entities.appointment.exceptions.AppointmentNotFound;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.domain.entities.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment getById(UUID id) throws AppointmentNotFound;
    List<Appointment> getAppointmentsBySlot(UUID slotId);

    UUID save(Appointment appointment) throws SlotNotFoundException, PatientNotFoundException, DoctorNotFoundException, MedicalConcernNotFoundException, QuestionNotFoundException;

    void confirm(Appointment appointment) throws SlotNotFoundException, PatientNotFoundException, DoctorNotFoundException, MedicalConcernNotFoundException, QuestionNotFoundException;

    void delete(UUID id);

    List<Appointment> getAllByUserAndStatusOrderByDateAsc(User user, AppointmentStatus status, int page, int size);

    List<Appointment> getAllByUserAndStatusOrderByDateDesc(User user, AppointmentStatus status, int page, int size);

    Optional<Appointment> getMostRecentUpcomingAppointment(User user);
}
