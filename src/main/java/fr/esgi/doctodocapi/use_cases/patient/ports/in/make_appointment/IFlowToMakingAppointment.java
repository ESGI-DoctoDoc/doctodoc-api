package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetAppointmentAvailabilityResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetCareTrackingForAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.doctor_questions.GetDoctorQuestionsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IFlowToMakingAppointment {
    List<GetMedicalConcernsResponse> getMedicalConcerns(UUID doctorId);
    List<GetDoctorQuestionsResponse> getMedicalConcernQuestions(UUID medicalConcernId);
    List<GetAppointmentAvailabilityResponse> getAppointmentsAvailability(UUID medicalConcernId, LocalDate date);

    List<GetCareTrackingForAppointmentResponse> getCareTracking();

}
