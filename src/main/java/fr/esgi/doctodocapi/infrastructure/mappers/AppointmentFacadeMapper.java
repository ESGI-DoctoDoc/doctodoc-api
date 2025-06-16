package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PreAppointmentAnswersEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentFacadeMapper {
    private final AppointmentMapper appointmentMapper;
    private final SlotMapper slotMapper;
    private final PatientMapper patientMapper;
    private final DoctorFacadeMapper doctorFacadeMapper;
    private final MedicalConcernMapper medicalConcernMapper;
    private final PreAppointmentAnswersMapper preAppointmentAnswersMapper;
    private final QuestionMapper questionMapper;

    public AppointmentFacadeMapper(AppointmentMapper appointmentMapper, SlotMapper slotMapper, PatientMapper patientMapper, DoctorFacadeMapper doctorFacadeMapper, MedicalConcernMapper medicalConcernMapper, PreAppointmentAnswersMapper preAppointmentAnswersMapper, QuestionMapper questionMapper) {
        this.appointmentMapper = appointmentMapper;
        this.slotMapper = slotMapper;
        this.patientMapper = patientMapper;
        this.doctorFacadeMapper = doctorFacadeMapper;
        this.medicalConcernMapper = medicalConcernMapper;
        this.preAppointmentAnswersMapper = preAppointmentAnswersMapper;
        this.questionMapper = questionMapper;
    }

    public Appointment mapAppointmentToDomain(AppointmentEntity appointmentEntity) {
        Slot slot = this.slotMapper.toDomain(appointmentEntity.getSlot());
        Patient patient = this.patientMapper.toDomain(appointmentEntity.getPatient());
        Doctor doctor = this.doctorFacadeMapper.mapDoctorToDomain(appointmentEntity.getDoctor());
        MedicalConcern medicalConcern = this.medicalConcernMapper.toDomain(appointmentEntity.getMedicalConcern());

        List<PreAppointmentAnswersEntity> answersEntities = appointmentEntity.getAppointmentQuestions();
        List<PreAppointmentAnswers> answers = answersEntities.stream().map(entity -> {
            Question question = this.questionMapper.toDomain(entity.getQuestion());
            return this.preAppointmentAnswersMapper.toDomain(entity, question);
        }).toList();

        return this.appointmentMapper.toDomain(appointmentEntity, slot, patient, doctor, medicalConcern, answers);
    }
}
