package fr.esgi.doctodocapi.use_cases.appointment;

import fr.esgi.doctodocapi.dtos.requests.save_appointment_request.SaveAnswersForAnAppointmentRequest;
import fr.esgi.doctodocapi.dtos.requests.save_appointment_request.SaveAppointmentRequest;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValidateAppointment {
    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public ValidateAppointment(SlotRepository slotRepository, PatientRepository patientRepository, MedicalConcernRepository medicalConcernRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public void confirm(SaveAppointmentRequest saveAppointmentRequest) {
        try {
            Slot slot = this.slotRepository.getById(saveAppointmentRequest.slotId());
            Patient patient = this.patientRepository.getById(saveAppointmentRequest.patientId());
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(saveAppointmentRequest.medicalConcernId());
            Doctor doctor = this.doctorRepository.getById(saveAppointmentRequest.doctorId());

            verifyIfMedicalConcernIsAuthorized(slot, medicalConcern);

            List<SaveAnswersForAnAppointmentRequest> answers = saveAppointmentRequest.responses();
            // todo get questions

            Appointment appointment = Appointment.init(slot, patient, doctor, medicalConcern, saveAppointmentRequest.time());
            this.appointmentRepository.save(appointment);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }

    public void confirm(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getById(id);
            appointment.confirm();

            this.appointmentRepository.save(appointment);
            // todo send an email to inform the patient and if it's not main account, send to user
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private void verifyIfMedicalConcernIsAuthorized(Slot slot, MedicalConcern medicalConcern) {
        if (!slot.getAvailableMedicalConcerns().contains(medicalConcern)) {
            throw new MedicalConcernNotAuthorizedExecption();
        }
    }
}
