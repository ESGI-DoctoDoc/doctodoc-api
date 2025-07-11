package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment getById(UUID id) throws AppointmentNotFoundException;

    List<Appointment> getAppointmentsBySlot(UUID slotId);

    UUID save(Appointment appointment);

    void cancel(Appointment appointment);

    void confirm(Appointment appointment) throws AppointmentNotFoundException;

    void complete(Appointment appointment) throws AppointmentNotFoundException;

    void delete(Appointment appointment);

    List<Appointment> getAllByUserAndStatusOrderByDateAsc(User user, AppointmentStatus status, int page, int size);

    List<Appointment> getAllByUserAndStatusOrderByDateDesc(User user, AppointmentStatus status, int page, int size);

    Optional<Appointment> getMostRecentUpcomingAppointment(User user);

    List<Patient> getDistinctPatientsByDoctorId(UUID doctorId, int page, int size);


    boolean existsPatientByDoctorAndPatientId(UUID doctorId, UUID patientId);

    List<Appointment> findAllWithPaginationForAdmin(int page, int size);

    int countAppointmentsByDoctorId(UUID doctorId);

    int countDistinctPatientsByDoctorId(UUID doctorId);

    List<Appointment> findVisibleAppointmentsByDoctorId(UUID doctorId, List<String> validStatuses, int page, int size);

    List<Appointment> findVisibleAppointmentsByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, List<String> validStatuses, int page, int size);

    Appointment getVisibleById(UUID id, List<String> validStatuses) throws AppointmentNotFoundException;

    List<Appointment> searchAppointmentsByPatientName(String patientName, int page, int size);

    List<Appointment> searchAppointmentsByDoctorAndPatientName(UUID doctorId, String patientName, int page, int size);

    List<Appointment> findAppointmentsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}
