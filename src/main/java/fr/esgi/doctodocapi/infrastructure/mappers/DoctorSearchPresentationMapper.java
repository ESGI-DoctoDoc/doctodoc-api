package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorMedicalConcernResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search.GetSearchAppointmentResponse;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class DoctorSearchPresentationMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetDoctorCareTrackingResponse toCareTrackingResponse(CareTracking careTracking) {
        Patient patient = careTracking.getPatient();
        return new GetDoctorCareTrackingResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                new GetDoctorCareTrackingResponse.CareTrackingPatient(
                        patient.getId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getEmail().getValue(),
                        patient.getPhoneNumber().getValue()
                ),
                careTracking.getClosedAt() != null ? careTracking.getClosedAt().toLocalDate().toString() : null
        );
    }

    public GetDoctorMedicalConcernResponse toMedicalConcernResponse(MedicalConcern concern) {
        return new GetDoctorMedicalConcernResponse(
                concern.getId(),
                concern.getName(),
                concern.getDurationInMinutes().getValue(),
                concern.getPrice(),
                concern.getQuestions().stream()
                        .map(GetQuestionResponse::fromDomain)
                        .toList()
        );
    }

    public GetDoctorPatientResponse toPatientResponse(Patient patient) {
        return new GetDoctorPatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail().getValue(),
                patient.getPhoneNumber().getValue(),
                patient.getGender().name().toLowerCase()
        );
    }

    public GetSearchAppointmentResponse toAppointmentDto(Appointment appointment) {
        var doctor = appointment.getDoctor();
        var doctorUser = doctor.getPersonalInformations();
        var patient = appointment.getPatient();
        var concern = appointment.getMedicalConcern();

        return new GetSearchAppointmentResponse(
                appointment.getId(),
                new GetSearchAppointmentResponse.AppointmentDoctor(
                        doctor.getId(),
                        doctorUser.getFirstName(),
                        doctorUser.getLastName(),
                        doctor.getEmail().getValue()
                ),
                new GetSearchAppointmentResponse.AppointmentPatient(
                        patient.getId(),
                        patient.getFirstName() + " " + patient.getLastName(),
                        patient.getEmail().getValue(),
                        patient.getPhoneNumber().getValue(),
                        patient.getBirthdate().toString()
                ),
                new GetSearchAppointmentResponse.AppointmentMedicalConcern(
                        concern.getId(),
                        concern.getName()
                ),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getHoursRange().getEnd().toString(),
                appointment.getStatus().getValue(),
                appointment.getTakenAt().format(DATE_FORMATTER)
        );
    }
}