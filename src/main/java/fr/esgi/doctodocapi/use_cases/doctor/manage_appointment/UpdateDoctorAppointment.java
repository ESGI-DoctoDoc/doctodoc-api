package fr.esgi.doctodocapi.use_cases.doctor.manage_appointment;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_appointment.UpdateDoctorAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.SaveDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment.IUpdateDoctorAppointment;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.IGetDoctorFromContext;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateDoctorAppointment implements IUpdateDoctorAppointment {
    private final IGetDoctorFromContext getDoctorFromContext;
    private final AppointmentRepository appointmentRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final SlotRepository slotRepository;

    public UpdateDoctorAppointment(IGetDoctorFromContext getDoctorFromContext, AppointmentRepository appointmentRepository, MedicalConcernRepository medicalConcernRepository, SlotRepository slotRepository) {
        this.getDoctorFromContext = getDoctorFromContext;
        this.appointmentRepository = appointmentRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.slotRepository = slotRepository;
    }

    public SaveDoctorAppointmentResponse execute(UUID appointmentId, UpdateDoctorAppointmentRequest request) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();

            Appointment appointment = appointmentRepository.getById(appointmentId);

            MedicalConcern medicalConcern = medicalConcernRepository.getMedicalConcernById(request.medicalConcernId(), doctor);
            Slot slot = slotRepository.findOneByMedicalConcernAndDate(medicalConcern.getId(), request.start());

            List<PreAppointmentAnswers> answers = extractAnswers(request.answers(), medicalConcern);

            appointment.update(
                    slot,
                    medicalConcern,
                    request.start(),
                    request.startHour(),
                    answers,
                    request.notes(),
                    request.careTrackingId()
            );
            this.appointmentRepository.update(appointment);

            return new SaveDoctorAppointmentResponse(appointment.getId());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private List<PreAppointmentAnswers> extractAnswers(List<UpdateDoctorAppointmentRequest.UpdateDoctorAnswersForAnAppointmentRequest> responses, MedicalConcern medicalConcern) {
        List<PreAppointmentAnswers> answers = new ArrayList<>();
        List<UUID> validQuestionIds = medicalConcernRepository.getDoctorQuestions(medicalConcern).stream()
                .map(Question::getId)
                .toList();

        for (UpdateDoctorAppointmentRequest.UpdateDoctorAnswersForAnAppointmentRequest response : responses) {
            Question question = medicalConcernRepository.getQuestionById(response.questionId());
            if (question == null || !validQuestionIds.contains(question.getId())) {
                throw new QuestionNotFoundException();
            }
            answers.add(PreAppointmentAnswers.of(question, response.answer()));
        }

        return answers;
    }
}
