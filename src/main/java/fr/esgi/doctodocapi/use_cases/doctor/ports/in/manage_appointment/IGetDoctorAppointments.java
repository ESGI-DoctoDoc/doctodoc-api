package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentDetailsResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IGetDoctorAppointments {
    List<GetDoctorAppointmentResponse> execute(int page, int size, LocalDate startDate);
    GetDoctorAppointmentDetailsResponse getById(UUID id);
}
