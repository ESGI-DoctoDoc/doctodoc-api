package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.DoctorInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.GetCareTrackingForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.PatientInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.AnswerInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments.GetAppointmentForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_medical_concerns.MedicalConcernInfoForAdmin;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Service
public class AppointmentResponseMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetDoctorAppointmentResponse toResponse(Appointment appointment, CareTracking careTracking) {
        return new GetDoctorAppointmentResponse(
                appointment.getId(),
                buildDoctorInfo(appointment),
                buildPatientInfo(appointment),
                buildMedicalConcernInfo(appointment),
                mapCareTrackingGeneric(careTracking, GetCareTrackingResponse::fromDomain),
                mapAnswersGeneric(appointment, answer -> new AnswerInfo(answer.getQuestion().getId(), answer.getResponse())),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getHoursRange().getEnd().toString(),
                appointment.getStatus().getValue(),
                appointment.getDoctorNotes(),
                formatDate(appointment.getTakenAt())
        );
    }

    public GetAppointmentForAdminResponse toAdminResponse(Appointment appointment, CareTracking careTracking) {
        return new GetAppointmentForAdminResponse(
                appointment.getId(),
                buildDoctorInfoForAdmin(appointment),
                buildPatientInfoForAdmin(appointment),
                buildMedicalConcernInfoForAdmin(appointment),
                mapCareTrackingGeneric(careTracking, GetCareTrackingForAdminResponse::fromDomain),
                mapAnswersGeneric(appointment, answers -> new AnswerInfoForAdmin(answers.getQuestion().getId(), answers.getResponse())),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getHoursRange().getEnd().toString(),
                appointment.getStatus().getValue(),
                appointment.getDoctorNotes(),
                formatDate(appointment.getTakenAt())
        );
    }

    private DoctorInfoForAppointment buildDoctorInfo(Appointment appointment) {
        Doctor doctor = appointment.getDoctor();
        return new DoctorInfoForAppointment(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getEmail().getValue()
        );
    }

    private DoctorInfoForAdmin buildDoctorInfoForAdmin(Appointment appointment) {
        Doctor doctor = appointment.getDoctor();
        return new DoctorInfoForAdmin(
                doctor.getId(),
                doctor.getPersonalInformations().getFirstName(),
                doctor.getPersonalInformations().getLastName(),
                doctor.getEmail().getValue()
        );
    }

    private PatientInfo buildPatientInfo(Appointment appointment) {
        Patient patient = appointment.getPatient();
        return new PatientInfo(
                patient.getId(),
                getPatientFullName(appointment),
                patient.getEmail().getValue(),
                patient.getPhoneNumber().getValue(),
                patient.getBirthdate().getValue().toString()
        );
    }

    private PatientInfoForAdmin buildPatientInfoForAdmin(Appointment appointment) {
        Patient patient = appointment.getPatient();
        return new PatientInfoForAdmin(
                patient.getId(),
                getPatientFullName(appointment),
                patient.getEmail().getValue(),
                patient.getPhoneNumber().getValue(),
                patient.getBirthdate().getValue().toString()
        );
    }

    private MedicalConcernInfo buildMedicalConcernInfo(Appointment appointment) {
        MedicalConcern concern = appointment.getMedicalConcern();
        return new MedicalConcernInfo(concern.getId(), concern.getName());
    }

    private MedicalConcernInfoForAdmin buildMedicalConcernInfoForAdmin(Appointment appointment) {
        MedicalConcern concern = appointment.getMedicalConcern();
        return new MedicalConcernInfoForAdmin(concern.getId(), concern.getName());
    }

    private <T> List<T> mapAnswersGeneric(Appointment appointment, Function<PreAppointmentAnswers, T> mapper) {
        return appointment.getPreAppointmentAnswers() == null
                ? null
                : appointment.getPreAppointmentAnswers().stream().map(mapper).toList();
    }

    private <T> T mapCareTrackingGeneric(CareTracking careTracking, Function<CareTracking, T> mapper) {
        return careTracking == null ? null : mapper.apply(careTracking);
    }

    private String getPatientFullName(Appointment appointment) {
        return String.format("%s %s",
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName());
    }

    private String formatDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
}