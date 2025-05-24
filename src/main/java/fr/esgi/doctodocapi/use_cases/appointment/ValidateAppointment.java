package fr.esgi.doctodocapi.use_cases.appointment;

import fr.esgi.doctodocapi.dtos.requests.save_appointment_request.SaveAnswersForAnAppointmentRequest;
import fr.esgi.doctodocapi.dtos.requests.save_appointment_request.SaveAppointmentRequest;
import fr.esgi.doctodocapi.dtos.responses.LockedAppointmentResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public LockedAppointmentResponse lock(SaveAppointmentRequest saveAppointmentRequest) {
        try {
            Slot slot = this.slotRepository.getById(saveAppointmentRequest.slotId());
            Patient patient = this.patientRepository.getById(saveAppointmentRequest.patientId());
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(saveAppointmentRequest.medicalConcernId());
            Doctor doctor = this.doctorRepository.getById(saveAppointmentRequest.doctorId());

            slot.validateIfSlotIsAuthorized(medicalConcern);

            List<PreAppointmentAnswers> answers = new ArrayList<>();
            if (saveAppointmentRequest.responses() != null) {
                answers = extractedPreAppointmentAnswers(saveAppointmentRequest);
            }

            Appointment appointment = Appointment.init(slot, patient, doctor, medicalConcern, saveAppointmentRequest.time(), answers);
            UUID appointmentLockedId = this.appointmentRepository.save(appointment);

            return new LockedAppointmentResponse(appointmentLockedId);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private List<PreAppointmentAnswers> extractedPreAppointmentAnswers(SaveAppointmentRequest saveAppointmentRequest) {
        List<PreAppointmentAnswers> answers = new ArrayList<>();

        List<SaveAnswersForAnAppointmentRequest> answersForAnAppointmentRequests = saveAppointmentRequest.responses();
        answersForAnAppointmentRequests.forEach(request -> {
            Question question = this.medicalConcernRepository.getQuestionById(request.questionId());
            answers.add(PreAppointmentAnswers.of(question, request.answer()));
        });

        return answers;
    }

    public void confirm(UUID id) {
        try {
            Appointment appointment = this.appointmentRepository.getById(id);
            appointment.confirm();
            this.appointmentRepository.confirm(appointment);
            // todo send an email to inform the patient and if it's not main account, send to user
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

}
