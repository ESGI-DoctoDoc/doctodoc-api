package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentAvailabilityResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IGetAppointmentsAvailabilityForDoctor {
    List<GetDoctorAppointmentAvailabilityResponse> execute(UUID medicalConcernId, LocalDate date);
}
