package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import fr.esgi.doctodocapi.presentation.patient.dtos.responses.appointment.GetAppointmentResponse;

import java.util.List;

public interface IGetAppointments {
    List<GetAppointmentResponse> getAllPastAppointments(int page, int size);
    List<GetAppointmentResponse> getAllUpcomingAppointments(int page, int size);
    GetAppointmentResponse getMostRecentUpcomingAppointment();
}
