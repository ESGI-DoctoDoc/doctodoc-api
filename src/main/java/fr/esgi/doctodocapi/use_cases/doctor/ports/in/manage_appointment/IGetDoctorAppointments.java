package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;

import java.util.List;

public interface IGetDoctorAppointments {
    List<GetDoctorAppointmentResponse> execute(int page, int size);
}
